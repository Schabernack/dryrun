#!/usr/bin/env python

import pygame
import time
from flask import Flask, request

app = Flask(__name__)


@app.route('/startmusic')
def start_music():
    pygame.mixer.music.play()
    return "OK"

@app.route('/stopmusic')
def stop_music():
    pygame.mixer.music.stop()
    return "OK"

if __name__=='__main__':
    pygame.mixer.init()
    pygame.mixer.music.load("clouds.mp3")
    app.run(debug = True, host = "0.0.0.0")

