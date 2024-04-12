format pe gui
entry start

include 'win32a.inc'

OFN_OVERWRITEPROMPT    EQU 00000002h
OFN_HIDEREADONLY       EQU 00000004h
OFN_PATHMUSTEXIST      EQU 00000800h
OFN_FILEMUSTEXIST      EQU 00001000h
OFN_EXPLORER	       EQU 00080000h
OFN_NODEREFERENCELINKS EQU 00100000h
OFN_ENABLESIZING       EQU 00800000h
OFN_FORCESHOWHIDDEN    EQU 10000000h

STYLE_WINDOW EQU WS_VISIBLE or WS_OVERLAPPEDWINDOW\
  or WS_POPUP or DS_CENTER or DS_SETFOREGROUND or DS_3DLOOK
STYLE_EDITBOX EQU WS_CHILD or WS_VISIBLE or WS_TABSTOP\
  or WS_BORDER

WINDOW_NAME EQU 'Windows App'
EDITBOX_NAME EQU ""
EDITBOX_CLASSNAME EQU 'EDIT'

IDC_EDITBOX EQU 101
IDC_DIALOG EQU 102
IDC_MENU EQU 1000
ID_PLIK EQU 1001
ID_NOWY EQU 1010
ID_OTWORZ EQU 1020
ID_ZAKONCZ EQU 1050

MENU_PLIK_NAME EQU '&Plik'
MENU_NOWY_NAME EQU '&Nowy'
MENU_OTWORZ_NAME EQU '&Otworz'
MENU_ZAKONCZ_NAME EQU 'Za&koñcz'

section '.data' data readable writeable
  hThisInstance dd 0
  bufor: times 100 db 0
  sizeof_bufor dd $ - bufor
  nowy db 0
  openFileName OPENFILENAME
  lpstrFilter db 'Pliki tekstowe', 0, '*.txt', 0
    db 'Wszyatkie pliki', 0, '*.*', 0
  lpstrCustomFilter: times 256 db 0
  nMaxCustFilter dd $ - lpstrCustomFilter
  lpstrFile db 'test.txt', 0
    times 256 db 0
  lpstrFileTitle: times 256 db 0
  nMaxFileTitle dd $ - lpstrFileTitle
  lpstrInitialDir db 'C:\', 0
  lpstrTitle db 'Otwórz plik', 0
  plik_uchwyt dd ?
  plik_liczba_odczytanych dd ?

section '.text' code readable executable
start:
  push 0
  call [GetModuleHandleA]
  mov [hThisInstance], eax
  push 0
  push WindowProcedure
  push 0
  push IDC_DIALOG
  push eax
  call [DialogBoxParamA]
  push 0
  call [ExitProcess]

proc WindowProcedure
  push ebp
  mov ebp, esp
  push ebx
  push esi
  push edi
  cmp dword [ebp + 0ch], WM_INITDIALOG
  je wm_initdialog
  cmp dword [ebp + 0ch], WM_COMMAND
  je wm_command
  cmp dword [ebp + 0ch], WM_CLOSE
  je wm_close
  jmp return
wm_initdialog:
  push 0
  push [hThisInstance]
  call [LoadIconA]
  push eax
  push 0
  push WM_SETICON
  push dword [ebp + 8h]
  call [SendMessageA]
  jmp return
wm_command:
  cmp word [ebp + 10h], ID_NOWY
  je menu_nowy
  cmp word [ebp + 10h], ID_OTWORZ
  je menu_otworz
  cmp word [ebp + 10h], ID_ZAKONCZ
  je menu_zakoncz
  jmp dalej
menu_nowy:
  push nowy
  push IDC_EDITBOX
  push dword [ebp + 8h]
  call [SetDlgItemTextA]
  jmp dalej
menu_otworz:
  mov [openFileName.lStructSize], sizeof.OPENFILENAME
  push dword [ebp + 8h]
  pop [openFileName.hwndOwner]
  push [hThisInstance]
  pop [openFileName.hInstance]
  mov [openFileName.lpstrFilter], lpstrFilter
  mov [openFileName.lpstrCustomFilter], lpstrCustomFilter
  push eax
  mov eax, [nMaxCustFilter]
  mov [openFileName.nMaxCustFilter], eax
  mov [openFileName.nFilterIndex], 0
  mov [openFileName.lpstrFile], lpstrFile
  mov [openFileName.nMaxFile], 256
  mov [openFileName.lpstrFileTitle], lpstrFileTitle
  mov eax, [nMaxFileTitle]
  mov [openFileName.nMaxFileTitle], eax
  pop eax
  mov [openFileName.lpstrInitialDir], lpstrInitialDir
  mov [openFileName.lpstrTitle], lpstrTitle
  mov [openFileName.Flags], OFN_ENABLESIZING\
    or OFN_EXPLORER or OFN_FORCESHOWHIDDEN\
    or OFN_PATHMUSTEXIST or OFN_OVERWRITEPROMPT\
    or OFN_HIDEREADONLY or OFN_FILEMUSTEXIST\
    or OFN_NODEREFERENCELINKS
  mov [openFileName.nFileOffset], 0
  mov [openFileName.lpfnHook], 0
  mov [openFileName.lpTemplateName], 0
  push openFileName
  call [GetOpenFileNameA]
  cmp eax, 0
  je dalej
  push 0
  push 0
  push OPEN_EXISTING
  push 0
  push 0
  push GENERIC_READ
  push lpstrFile
  call [CreateFileA]
  mov [plik_uchwyt], eax
  cmp eax, 0
  je plik_zamknij
  push 0
  push plik_liczba_odczytanych
  push [sizeof_bufor]
  push bufor
  push [plik_uchwyt]
  call [ReadFile]
  cmp eax, 0
  je plik_zamknij
  push bufor
  push IDC_EDITBOX
  push dword [ebp + 8h]
  call [SetDlgItemTextA]
plik_zamknij:
  push [plik_uchwyt]
  call [CloseHandle]
  jmp dalej
menu_zakoncz:
  push 0
  push 0
  push WM_CLOSE
  push dword [ebp + 8h]
  call [SendMessageA]
dalej:
  jmp return
wm_close:
  push 0
  push dword [ebp + 8h]
  call [EndDialog]
return:
  xor eax, eax
  pop edi
  pop esi
  pop ebx
  pop ebp
  ret
endp

section '.rsrc' data readable resource
  directory\
    RT_DIALOG, dialogs,\
    RT_MENU, menus

  resource dialogs, IDC_DIALOG,\
    LANG_ENGLISH + SUBLANG_DEFAULT, szClassName
  dialog szClassName, WINDOW_NAME, 10, 10, 297, 190,\
    STYLE_WINDOW, WS_EX_STATICEDGE, IDC_MENU, 'MS Sans Serif', 8
    dialogitem EDITBOX_CLASSNAME, EDITBOX_NAME,\
      IDC_EDITBOX, 15, 12, 256, 162, STYLE_EDITBOX, 0
  enddialog

  resource menus, IDC_MENU, LANG_ENGLISH + SUBLANG_DEFAULT,\
    menu_plik
  menu menu_plik
    menuitem MENU_PLIK_NAME, ID_PLIK, MFR_POPUP + MFR_END
      menuitem MENU_NOWY_NAME, ID_NOWY
      menuitem MENU_OTWORZ_NAME, ID_OTWORZ
      menuseparator
      menuitem MENU_ZAKONCZ_NAME, ID_ZAKONCZ, MFR_END

section '.idata' import data readable writeable
  library\
    kernel, 'kernel32.dll',\
    user, 'user32.dll',\
    comdlg, 'comdlg32.dll'

  import kernel,\
    GetModuleHandleA, 'GetModuleHandleA',\
    ExitProcess, 'ExitProcess',\
    CreateFileA, 'CreateFileA',\
    ReadFile, 'ReadFile',\
    CloseHandle, 'CloseHandle'

  import user,\
    LoadIconA, 'LoadIconA',\
    SendMessageA, 'SendMessageA',\
    DialogBoxParamA, 'DialogBoxParamA',\
    EndDialog, 'EndDialog',\
    SendDlItemMessageA, 'SendDlgItemMessageA',\
    SetDlgItemTextA, 'SetDlgItemTextA'

  import comdlg,\
    GetOpenFileNameA, 'GetOpenFileNameA'
