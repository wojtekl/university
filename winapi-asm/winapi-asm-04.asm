format pe gui
entry start

include 'win32a.inc'

STYLE_BUTTON EQU WS_CHILD or WS_VISIBLE or\
  BS_DEFPUSHBUTTON or WS_TABSTOP
STYLE_LISTBOX EQU WS_CHILD or WS_VISIBLE or\
  WS_THICKFRAME or WS_BORDER or WS_TABSTOP or WS_VSCROLL
STYLE_EDITBOX EQU WS_CHILD or WS_VISIBLE or WS_BORDER or\
  WS_TABSTOP

section '.bss' data readable writeable
  hThisInstance dd 0
  wincl WNDCLASS
  szClassName db "WindowsApp", 0
  windowName db "Windows App", 0
  hwnd dd ?
  messages MSG
  buttonName db "Przycisk", 0
  buttonClassName db "BUTTON", 0
  buttonHwnd dd ?
  listBoxName db "", 0
  listBoxClassName db "LISTBOX", 0
  listBoxHwnd dd ?
  editBoxName db "Pole tekstowe", 0
  editBoxClassName db "EDIT", 0
  editBoxHwnd dd ?
  bufor: times 100 db 0

section '.text' code readable executable
start:
  push 0
  call [GetModuleHandleA]
  mov [hThisInstance], eax
  mov [wincl.hInstance], eax
  mov [wincl.lpszClassName], szClassName
  mov [wincl.lpfnWndProc], WindowProcedure
  mov [wincl.style], CS_DBLCLKS
  push IDI_APPLICATION
  push 0
  call [LoadIconA]
  mov [wincl.hIcon], eax
  push IDC_ARROW
  push 0
  call [LoadCursorA]
  mov [wincl.hCursor], eax
  mov [wincl.lpszMenuName], 0
  mov [wincl.cbClsExtra], 0
  mov [wincl.cbWndExtra], 0
  mov [wincl.hbrBackground], COLOR_BACKGROUND
  push wincl
  call [RegisterClassA]
  push 0
  push [hThisInstance]
  push 0
  push HWND_DESKTOP
  push 375
  push 544
  push CW_USEDEFAULT
  push CW_USEDEFAULT
  push WS_OVERLAPPEDWINDOW
  push windowName
  push szClassName
  push 0
  call [CreateWindowExA]
  cmp eax, 0
  jz koniec
  mov [hwnd], eax
  push SW_SHOWNORMAL
  push [hwnd]
  call [ShowWindow]
  push [hwnd]
  call [UpdateWindow]
while_messages:
  push 0
  push 0
  push 0
  push messages
  call [GetMessageA]
  cmp eax, 0
  jz koniec
  push messages
  call [TranslateMessage]
  push messages
  call [DispatchMessageA]
  jmp while_messages
koniec:
  push [messages.wParam]
  call [ExitProcess]

proc WindowProcedure
  push ebp
  mov ebp, esp
  push ebx
  push esi
  push edi
  cmp dword [ebp + 0ch], WM_CREATE
  je wm_create
  cmp dword [ebp + 0ch], WM_COMMAND
  je wm_command
  cmp dword [ebp + 0ch], WM_DESTROY
  je wm_destroy
  jmp default
wm_create:
; button
  push 0
  push [hThisInstance]
  push 0
  push dword [ebp + 8h]
  push 20
  push 80
  push 10
  push 200
  push STYLE_BUTTON
  push buttonName
  push buttonClassName
  push 0
  call [CreateWindowExA]
  mov [buttonHwnd], eax
; listbox
  push 0
  push [hThisInstance]
  push 0
  push dword [ebp + 8h]
  push 90
  push 150
  push 40
  push 20
  push STYLE_LISTBOX
  push listBoxName
  push listBoxClassName
  push 0
  call [CreateWindowExA]
  mov [listBoxHwnd], eax
; editbox
  push 0
  push [hThisInstance]
  push 0
  push dword [ebp + 8h]
  push 20
  push 80
  push 10
  push 10
  push STYLE_EDITBOX
  push editBoxName
  push editBoxClassName
  push 0
  call [CreateWindowExA]
  mov [editBoxHwnd], eax
  push eax
  call [SetFocus]
  jmp case_break
wm_command:
  mov eax, [buttonHwnd]
  cmp dword [ebp + 14h], eax
  jne case_break
  push bufor
  push 100
  push WM_GETTEXT
  push [editBoxHwnd]
  call [SendMessageA]
  push bufor
  push 0
  push LB_ADDSTRING
  push [listBoxHwnd]
  call [SendMessageA]
  jmp case_break
wm_destroy:
  push 0
  call [PostQuitMessage]
  jmp case_break
default:
  push dword [ebp + 14h]
  push dword [ebp + 10h]
  push dword [ebp + 0ch]
  push dword [ebp + 8h]
  call [DefWindowProcA]
return:
  pop edi
  pop esi
  pop ebx
  pop ebp
  ret
case_break:
  xor eax, eax
  jmp return
endp

section '.idata' import data readable writeable
  library\
    kernel, 'kernel32.dll',\
    user, 'user32.dll'

  import kernel,\
    GetModuleHandleA, 'GetModuleHandleA',\
    ExitProcess, 'ExitProcess'

  import user,\
    LoadIconA, 'LoadIconA',\
    LoadCursorA, 'LoadCursorA',\
    RegisterClassA, 'RegisterClassA',\
    CreateWindowExA, 'CreateWindowExA',\
    ShowWindow, 'ShowWindow',\
    UpdateWindow, 'UpdateWindow',\
    GetMessageA, 'GetMessageA',\
    TranslateMessage, 'TranslateMessage',\
    DispatchMessageA, 'DispatchMessageA',\
    DefWindowProcA, 'DefWindowProcA',\
    PostQuitMessage, 'PostQuitMessage',\
    SendMessageA, 'SendMessageA',\
    SetFocus, 'SetFocus'