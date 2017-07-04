format mz

entry main:start

stack 100h

segment main

start:
  mov dx, text
  mov ds, dx
  
  mov ah, 9
  mov dx, info
  int 21h
  
  mov ah, 0
  int 16h
  
  mov ax, 4c00h
  int 21h
  
segment text
  
info db "Czesc.$"

