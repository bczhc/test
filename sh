#!/bin/bash
apt install ffmpeg -y
ffmpeg -i ./out.mp4 -filter "setpts=0.01*PTS" -q:v 0 out_2.mp4

