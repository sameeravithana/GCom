#!/bin/bash

cp client.properties ./build/classes 
cp server.properties ./build/classes 
cd ./build/classes
java gui.member.NewMember
