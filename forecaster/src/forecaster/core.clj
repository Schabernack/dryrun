(ns forecaster.core
  (:require [clojure.tools.logging :as log]
            [forecaster.alarms :as alarms]
            [ring.middleware
             [reload :as reload]
             [params :refer [wrap-params]]
             [json :refer [wrap-json-body wrap-json-response]]]
            [compojure
             [handler :as handler]
             [route :as route]]
            [org.httpkit.server :refer [run-server]]
            [clj-http.client :as client]
            [cheshire.core :refer :all]
            [compojure.core :refer :all])
  (:gen-class))

(defn response
  [body & {:keys [status headers]
           :or {status 200 headers {}}}]
  {:status status
   :headers headers
   :body body})

(defmacro json-wrapper
  [& body]
  (let [[status body] (if (number? (first body))
                        [(first body) (rest body)]
                        [200 body])]
    `(try
       (response ~@body :status ~status)
       (catch Exception e#
         (log/warn e# "Unknown Error")
         (response {:error "Unknown Error"} :status 500)))))

(defroutes app
  (POST "/alarms" req
    (let [alarm-spec (:body req)]
      (log/info "Creating alarm")
      (json-wrapper
       (alarms/create-alarm alarm-spec))))
  (POST "/alarms2" req
    (log/info "****" (pr-str req))
    (json-wrapper
     {:asfd "asfd"}))
  (GET "/alarms/:id" [id]
    (log/info "Getting alarm id:" id)
    (let [alarm (alarms/get-alarm (Long/parseLong id))]
      (if-not (empty? alarm)
        (json-wrapper alarm)
        (route/not-found "<h1>Page not found</h1>"))))
  (PUT "/alarms/:id" [id :as req]
    (let [id (Long/parseLong id)]
      (log/info "Updating alarm id:" id)
      (json-wrapper
       (alarms/update-alarm id (:body req)))))
  (GET "/alarms/:id/status" [id]
    (log/info "Getting alarm status" id)
    (json-wrapper
     (alarms/get-alarm-status (Long/parseLong id))))
  (route/resources "/")
  (route/not-found "<h1>Page not found</h1>"))

(def handler (-> app
                 handler/api
                 wrap-json-response
                 (wrap-json-body {:keywords? true})))

(defn -main [& args]
  (let [port (if (first args)
               (Integer/parseInt (first args))
               8080)
        mode (if (second args)
               (keyword (second args))
               :dev)]
    (log/info "Running server on port:" port)
    (run-server (if (= mode :dev)
                  (reload/wrap-reload #'handler)
                  handler) {:port port})))
