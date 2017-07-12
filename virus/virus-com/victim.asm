format binary

org 100h

start:
  mov dx, cs
  mov ds, dx
  
  mov ah, 9
  mov dx, info
  int 21h
  
  mov ah, 0
  int 16h
  
  mov ax, 4c00h
  int 21h
  
info db "Czesc.$"

