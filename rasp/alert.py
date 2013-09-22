#!/usr/bin/python

import pyaudio, audioop, numpy, pprint, urllib2

chunk = 1024
rate = 44100

def main():
    hostname = 'http://192.168.236.162:5000/'
    while True:
        if trigger():
            print("STOPMUSIC sent")
            urllib2.urlopen(hostname + 'stopmusic')
        trigger()

def trigger():
    triggered = False
    foo = record_sample()
    average_loud = sum(foo)/len(foo)
    print("AVERAGE: "+str(average_loud))
    framesize = 12
    #iterate over rms volumes, if 5consecutive values are high, trigger
    for idx in range(0,len(foo),1):
        if foo[idx] > average_loud and idx<len(foo)-5:
            frame = foo[idx:idx+5]
            #print(frame)
            if all(val > average_loud*3 for val in frame):
                triggered = True
    return triggered

def record_sample(seconds_to_record = 2):
    audio_rms = []
    p = pyaudio.PyAudio()


    stream = p.open(format=pyaudio.paInt8,
            channels=1,
            rate=rate,
            input=True,
            frames_per_buffer=chunk)

    for i in range(0,44100 / chunk*seconds_to_record):
        data = stream.read(chunk)
        rms = audioop.rms(data, 2)  #width=2 for format=paInt1
        audio_rms.append(rms)
    return audio_rms


if __name__ == '__main__':
    main()
