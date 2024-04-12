#!/usr/bin/python

class Punkt :
 
 x=0
 y=0
 z=0
 
 def __init__(self, x0, y0, z0) :
 
  self.x=x0
  self.y=y0
  self.z=z0

plik = open('untitled.obj', 'r');
linie = plik.readlines();

v=[]
vn=[]
n=""
ver=""
col=""

mapa = open('obj.txt', 'w')

ob = -1

naz = ""

for line in linie:
 
 if line[0:2] == 'o ' :
  
  if ob > -1 :
   
   mapa.write(naz)
   
  ob = ob + 1  

  naz = line
 
 if line[0:2] == 'v ' :
  
  l=[]
  
  for z in line[1:].split() :
   
   l.append(z)
  
  v.append(Punkt(l[0], l[1], l[2]))
 
 elif line[0:2] == 'vn' :
  
  l=[]
  
  for z in line[2:].split() :
   
   l.append(z)
  
  vn.append(Punkt(l[0], l[1], l[2]))
  
 elif line[0:2] == 'f ' :

  col = col + "1.0f 1.0f 1.0f 1.0f\n"
  
  l=[]
  i=0
  
  for z in line[1:].split() :
   
   t=z.replace('//', ' ').split()
   l.append(int(t[0])-1)

   n = n + vn[int(t[1])-1].x + "f " + vn[int(t[1])-1].y + "f " + vn[int(t[1])-1].z + "f\n"
   
   ver = ver + v[l[i]].x + "f " + v[l[i]].y + "f " + v[l[i]].z + "f\n"
   i=i+1

mapa.write(naz)

mapa.close()
