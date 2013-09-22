(ns forecaster.alarms
  (:require [clojure.data :refer [diff]]
            [forecaster.fetcher :as f]
            [clojure.tools.logging :as log])
  (:import java.util.concurrent.atomic.AtomicLong))

(defonce alarms (atom {}))

(defonce counter ^AtomicLong (AtomicLong. 0))

(defn create-alarm [alarm]
  (let [alarm (select-keys alarm [:alarm :alternate :precip-intensity :coordinates])
        alarm (if-not (coll? (:coordinates alarm))
                (assoc alarm :coordinates (keep identity [(:coordinates alarm)]))
                alarm)
        id (.getAndIncrement counter)]
    (swap! alarms assoc id (-> alarm
                               (assoc :id id)))
    (f/fetch-forecast-for-coordinate alarms (get @alarms id))
    (get @alarms id {:error "Unable to create alarm"})))

(defn update-alarm [id new-alarm]
  (let [old-alarm (@alarms id)
        new-alarm (dissoc new-alarm :id)
        [_ new both] (diff old-alarm new-alarm)
        things-to-update (merge new both)
        new-alarm (merge old-alarm things-to-update)]
    (swap! alarms assoc id new-alarm)
    things-to-update))

(defn get-alarm [id]
  (get @alarms id {}))

(defn delete-alarm [id]
  (swap! alarms dissoc id))

(defn get-alarm-status [id]
  {:status :ok})

(defn fire-alarm? [id]
  (let [alarm (get-alarm id)
        forecasts (:forecasts alarm)
        results (for [forecast forecasts]
                  (> (:precipProbability forecast) 0))]
    (some identity results)))
