#!/usr/bin/env python

import pygame
import time
#pygame.init()
pygame.mixer.init()
pygame.mixer.music.load("clouds.mp3")
pygame.mixer.music.play()
# sleep for the interval value when it is raining
time.sleep(5)
pygame.mixer.music.stop()
