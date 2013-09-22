(ns forecaster.fetcher
  (:require [clojure.tools.logging :as log]
            [clj-http.client :as client]
            [cheshire.core :refer :all]))

(def api-key "b316d8b6c6d7e1abd39ad2df3bd03119")

(defn generate-url [api-key lat lng]
  (format "https://api.forecast.io/forecast/%s/%s,%s?units=ca" api-key lng lat))

(defn weather-at-point [lat lng]

  (let [url (generate-url api-key lng lat)
        _ (log/info "Attempting to query:" url)
        raw (client/get url)]
    (-> raw
        :body
        (parse-string true)
        :currently)))

(defn fetch-forecast-for-coordinate [world alarm]
  (future (let [id (:id alarm)
                coordinates (get-in @world [id :coordinates] [])
                results (for [{:keys [lat lng]} coordinates]
                          (-> (weather-at-point lat lng)
                              (assoc :lat lat)
                              (assoc :lng lng)))]
            (log/info "Results for fetch:" (pr-str results))
            (swap! world assoc-in [id :forecast] results)
            (log/info "World:" (pr-str @world)))))
