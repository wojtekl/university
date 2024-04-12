clear;
clc;

function[W, E]=neuro(X1, X2, P)
    
    rand('seed', getdate('s'));
    
    c=size(X1, 'r');
    ec=2000;
    a=0.2;
    
    W=int(rand(3,3,'uniform')*80)/100+0.1;
    
    for e=1:1:ec
        
        E(e,:)=[e 0];
        
        for i=1:1:c
        
        S(1)=W(1,1)*X1(i)+W(1,2)*X2(i)+W(1,3);
        Y(1)=1/(1+exp(-S(1)));
        S(2)=W(2,1)*X1(i)+W(2,2)*X2(i)+W(2,3);
        Y(2)=1/(1+exp(-S(2)));
        
        S(3)=W(3,1)*Y(1)+W(3,2)*Y(2)+W(3,3);
        Y(3)=1/(1+exp(-S(3)));
        
        fp(3)=Y(3)*(1-Y(3));
        temp=P(i)-Y(3);
        E(e,2)=E(e,2)+temp*temp;
        d(3)=temp*fp(3);
        
        fp(1)=Y(1)*(1-Y(1));
        d(1)=(W(3,1)*d(3)+W(3,2)*d(3)+W(3,3)*d(3))*fp(1);
        fp(2)=Y(2)*(1-Y(2));
        d(2)=(W(3,1)*d(3)+W(3,2)*d(3)+W(3,3)*d(3))*fp(2);
        
        W(3,1)=W(3,1)+a*d(3)*Y(1);
        W(3,2)=W(3,2)+a*d(3)*Y(2);
        W(3,3)=W(3,3)+a*d(3);
        
        W(1,1)=W(1,1)+a*d(1)*X1(i);
        W(1,2)=W(1,2)+a*d(1)*X2(i);
        W(1,3)=W(1,3)+a*d(1);
        
        W(2,1)=W(2,1)+a*d(2)*X1(i);
        W(2,2)=W(2,2)+a*d(2)*X2(i);
        W(2,3)=W(2,3)+a*d(2);
        
    end
    
    E(e,2)=sqrt(E(e,2)/c);
        
    end
    
endfunction

close;
close;

cd(get_absolute_file_path("irysy2x1.sce"));
f=read('iris.dat',150,4);
for i=1:1:50
    IS(i,:)=f(i,1:4);
    IVC(i,:)=f(i+50,1:4);
    IV(i,:)=f(i+100,1:4);
end

X1=[IS(:,3); IVC(:,3)];
X2=[IS(:,4); IVC(:,4)];

P=[ones(1,50) zeros(1,50)];
c=size(X1,'r');
[W, E]=neuro(X1,X2,P);
for i=1:1:c
    if 1 == P(i)
        plot(X1(i),X2(i),'ro:');
    else
        plot(X1(i),X2(i),'bx:');
    end
end
XL=[0:10];
fun1=-W(1,1)/W(1,2)*XL-W(1,3)/W(1,2);
plot(XL,fun1,'k');
fun2=-W(2,1)/W(2,2)*XL-W(2,3)/W(2,2);
plot(XL,fun2,'k');
g1=gca();
xlabel(g1, 'długość płatka');
ylabel(g1, 'szerokość płatka');
scf();
plot(E(:,1),E(:,2),'k');
g2=gca();
xlabel(g2, 'liczba epok');
ylabel(g2, 'błąd na wyjściu sieci');
