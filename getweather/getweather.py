import json
import sys
import urllib2
from sys import argv

script, setPrecipIntensity = argv
weather = json.load(urllib2.urlopen("http://ec2-184-72-208-140.compute-1.amazonaws.com:8080/?lat=-122.423&long=37.8267"))
precipIntensity = weather['precipIntensity']

if precipIntensity == (int(setPrecipIntensity)): 
  print("Wake up for a dry run!")
else:
  print("Sleep in as it is wet")
