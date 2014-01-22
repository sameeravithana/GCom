#!/bin/bash

cp client.properties ./build/classes 
cp server.properties ./build/classes 
cp server.policy ./build/classes 
cd ./build/classes
java gui.member.NewMember 
