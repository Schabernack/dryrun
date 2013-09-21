(ns forecaster.core
  (:require [clj-http.client :as client]
            [cheshire.core :refer :all])
  (:gen-class))

(def token "b316d8b6c6d7e1abd39ad2df3bd03119")

(defn generate-url [token lat long]
  (format "https://api.forecast.io/forecast/%s/%s,%s" token lat long))

(defn weather-at-point [long lat]
  (let [raw (client/get (generate-url token long lat))]
    (-> raw
        :body
        (parse-string true)
        :currently)))

(defn -main [& args]
  (weather-at-point 37.8267 -122.423))
