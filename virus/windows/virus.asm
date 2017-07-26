format PE GUI 4.0

include 'win32a.inc'

  invoke MessageBoxA, 0h, message, caption, MB_ICONINFORMATION + MB_OK
  invoke ExitProcess, 0h
  
message db 'Jestem wirus }:->', 0h
caption db 'Wirus!', 0h

data import

  library kernel32, 'KERNEL32.DLL', user32, 'USER32.DLL'
    
  import kernel32, ExitProcess, 'ExitProcess'
  import user32, MessageBoxA, 'MessageBoxA'
  
end data
