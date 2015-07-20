#!/bin/sh
convert $1 -resize 240x240 drawable-mdpi/$1
convert $1 -resize 360x360 drawable-hdpi/$1
convert $1 -resize 480x480 drawable-xhdpi/$1
convert $1 -resize 720x720 drawable-xxhdpi/$1
mv $1 drawable-xxxhdpi/$1
