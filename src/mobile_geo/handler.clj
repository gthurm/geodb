(ns mobile-geo.handler
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [clj-http.lite.client :as http]
            [clojure.java.jdbc :as sql]
            [clojure.data.json :as json]
            [clojure.pprint :as pprint :refer [pprint]]))

(def geo-db {:subprotocol "mysql"
             :subname "//127.0.0.1:3306/ortsdaten"
             :user "mobile"
             :password ""})

(defn resolve-geo [country postcode]
  (sql/query geo-db ["select * from geodata where country = ? and zipcode like ?", country, postcode]))

(defn resolve-all-geo []
  (sql/query geo-db ["select * from geodata where country = 'DE' and city not like '% postfach'"]))

(defn make-location [g]
  {:name (:city g)
   :suggest {
     :input  [(:city g) (:zipcode g)]
     :output (str (:city g) ", " (:country g) "-" (:zipcode g))
     :payload {:pos {:lt (:latitude g) :ln (:longitude g)}}}})

(defn make-bulk-entry [g]
    (str 
      (json/write-str {:index {}})
      "\n"
      (json/write-str (make-location g))
      "\n"))

(defn auto-complete [q]
  (let [request {:suggest {:text q
                           :completion {:field "suggest"
                                        :size 20}}}]

    (http/post "http://localhost:9200/location/_suggest"
               {:body (json/write-str request) 
                :content-type :json})))
    
(defroutes app-routes
  (GET "/complete" [term] (auto-complete term))
  (GET "/location" []  (map make-bulk-entry (resolve-all-geo)))
  (route/resources "/"))

(def app
  (handler/site app-routes))
