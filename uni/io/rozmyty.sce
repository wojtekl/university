clear;
clc;

function[Y]=sigma(X, b, s)
    l=size(X, "c");
    for i=1:1:l
        kw=(X(i)-b)/s;
        Y(1,i)=exp(-kw*kw);
    end
endfunction

function[Y]=cartes(A, B)
    for i=1:1:size(A, "c")
        for j=1:1:size(B, "c")
            Y(i, j)=min([A(i), B(j)]);
        end
    end
endfunction

function[Y]=sel(X1, X2, f)
    l1=size(X1, "c");
    l2=size(X2, "c");
    for i=1:1:l2
        if l1 > 1 then
            Y(1,i)=f([X1(i) X2(i)]);
        else
            Y(1,i)=f([X1 X2(i)]);
        end
    end
endfunction


close;
close;
//close;
close;
close;
close;


//scf();
A1=[30:0.1:43];
a11=1.3; b11=33.6;
A1(2,:)=sigma(A1(1,:), b11, a11);
//plot(A1(1,:), A1(2,:), 'k');
a12=1.3; b12=36.6;
A1(3,:)=sigma(A1(1,:), b12, a12);
//plot(A1(1,:), A1(3,:), 'k');
a13=1.3; b13=39.6;
A1(4,:)=sigma(A1(1,:), b13, a13);
//plot(A1(1,:), A1(4,:), 'k');
//g1=gca();
//xlabel(g1, 'niska                         w normie                       wysoka');

//scf();
A2=[0:2:240];
a21=25; b21=60;
A2(2,:)=sigma(A2(1,:), b21, a21);
//plot(A2(1,:), A2(2,:), 'k');
a22=25; b22=120;
A2(3,:)=sigma(A2(1,:), b22, a22);
//plot(A2(1,:), A2(3,:), 'k');
a23=25; b23=180;
A2(4,:)=sigma(A2(1,:), b23, a23);
//plot(A2(1,:), A2(4,:), 'k');
//g2=gca();
//xlabel(g2, 'niskie                              w normie                              wysokie');


//scf();
//mesh(cartes(A1(2,:), A2(3,:)));


x1=38;
X1(1)=sigma(x1, b11, a11);
X1(2)=sigma(x1, b12, a12);
X1(3)=sigma(x1, b13, a13);

x2=140;
X2(1)=sigma(x2, b21, a21);
X2(2)=sigma(x2, b22, a22);
X2(3)=sigma(x2, b23, a23);

 
//scf();
B=[1:0.1:10];
a11=2; b11=1;
B(2,:)=sigma(B(1,:), b11, a11);
//plot(B(1,:), B(2,:), 'k');
a12=2; b12=5;
B(3,:)=sigma(B(1,:), b12, a12);
//plot(B(1,:), B(3,:), 'k');
a13=2; b13=10;
B(4,:)=sigma(B(1,:), b13, a13);
//plot(B(1,:), B(4,:), 'k');
//g3=gca();
//xlabel(g3, 'osłabiony                                        w normie                                        pobudzony');


deff("[y]=minimum(X)", "y=min(X)");
deff("[y]=maximum(X)", "y=max(X)");
deff("[y]=iloczyn(X)", "y=X(1)*X(2)");


mi1=sel(X1(1), X2(1), minimum);
T1=[B(1,:); sel(mi1, B(2,:), minimum)];
scf();
plot(B(1,:), B(2,:), 'b');
plot2d3(T1(1,:), T1(2,:), 2);
plot(T1(1,:), T1(2,:), 'r');
mtlb_axis([min(T1(1,:)), max(T1(1,:)), 0, 1]);
mi2=sel(X1(2), X2(2), minimum);
T2=[B(1,:); sel(mi2, B(3,:), minimum)];
g4=gca();
xlabel(g4, 'stopień aktywacji reguły R1');
scf();
plot(B(1,:), B(3,:), 'b');
plot2d3(T2(1,:), T2(2,:), 2);
plot(T2(1,:), T2(2,:), 'r');
mtlb_axis([min(T2(1,:)), max(T2(1,:)), 0, 1]);
mi3=sel(X1(3), X2(3), minimum);
T3=[B(1,:); sel(mi3, B(4,:), minimum)];
g5=gca();
xlabel(g5, 'stopień aktywacji reguły R2');
scf();
plot(B(1,:), B(4,:), 'b');
plot2d3(T3(1,:), T3(2,:), 2);
plot(T3(1,:), T3(2,:), 'r');
mtlb_axis([min(T3(1,:)), max(T3(1,:)), 0, 1]);
g6=gca();
xlabel(g6, 'stopień aktywacji reguły R3');


TW=[B(1,:); sel(sel(T1(2,:), T2(2,:), maximum), T3(2,:), maximum)];
scf();
plot(TW(1,:), TW(2,:), 'k');
plot2d3(TW(1,:), TW(2,:), 2);
mtlb_axis([min(TW(1,:)), max(TW(1,:)), 0, 1]);


y=(b11*mi1+b12*mi2+b13*mi3)/(mi1+mi2+mi3);
plot([y y], [0 1], 'r');
disp(y);
g7=gca();
xlabel(g7, string(y));
