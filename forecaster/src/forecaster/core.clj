(ns forecaster.core
  (:require [clojure.tools.logging :as log]
            [ring.middleware
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

(def token "b316d8b6c6d7e1abd39ad2df3bd03119")

(defn generate-url [token lat long]
  (format "https://api.forecast.io/forecast/%s/%s,%s" token lat long))

(defn weather-at-point [lat long]
  (let [raw (client/get (generate-url token long lat))]
    (-> raw
        :body
        (parse-string true)
        :currently)))

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
  (GET "/" {params :query-params}
    (log/info (pr-str params))
    (json-wrapper
     (weather-at-point (params "lat") (params "long") )))
  (route/not-found "<h1>Page not found</h1>"))

(def handler (-> app
                 handler/api
                 wrap-json-response
                 (wrap-json-body {:keywords? true})))

(defn -main [& args]
  (let [port 8080]
    (log/info "Running server on port:" port)
    (run-server handler {:port port})))
