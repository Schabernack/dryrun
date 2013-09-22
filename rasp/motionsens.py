#!/usr/bin/env python

from os import listdir
from os.path import isfile, join
import urllib2

def get_filelist(mypath):
    onlyfiles = [ f for f in listdir(mypath) if isfile(join(mypath,f)) ]
    return onlyfiles

if __name__ == '__main__':
    mypath = '/home/suhaim/.motion'
    host = 'http://192.168.236.162:5000/'
   # mypath = '/users/nico/__scratch'
    onlyfiles = get_filelist(mypath)
    initial_no_of_files = len(onlyfiles)
    print('initial files: ' + str(initial_no_of_files))
    while True:
        file_no = len(get_filelist(mypath))
        print('files: ' + str(file_no))
        if file_no > initial_no_of_files:
            print("Sent stopmusic command")
            urllib2.urlopen(host + 'stopmusic')
            initial_no_of_files = file_no
            print('new initial: ' + str(initial_no_of_files))

    
