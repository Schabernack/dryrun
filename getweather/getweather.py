import json
import sys
import urllib2
from sys import argv

script, setPrecipIntensity = argv
weather = json.load(urllib2.urlopen("http://localhost:8080/?lat=-122.423&long=37.8267"))
precipIntensity = weather['precipIntensity']

if precipIntensity == (int(setPrecipIntensity)): 
  print("wake up for a dry run")
else:
  print("sleep in as it is wet")
