(ns forecaster.alarms
  (:import java.util.concurrent.atomic.AtomicLong))

(defonce alarms (atom {}))

(defonce counter ^AtomicLong (AtomicLong. 0))

(defn create-alarm [alarm]
  (let [alarm (select-keys alarm [:alarm :alternate :precip-intensity])
        id (.getAndIncrement counter)]
    (swap! alarms assoc id (-> alarm
                               (assoc :id id)))
    (get @alarms id {:error "Unable to create alarm"})))
