               entry
               addi r14, r0, topaddr
%storing 132 into lit1
               addi r1, r0, 132
               sw lit1(r0), r1
                addi r1, r0, 0
% assigning lit1 to x
               lw r1, lit1(r0)
               sw x(r0), r1
               addi r1, r0, 0
%storing 12 into lit2
               addi r1, r0, 12
               sw lit2(r0), r1
                addi r1, r0, 0
%multiplying x with lit2
               lw r1, x(r0)
               lw r2, lit2(r0)
               div r3, r1, r2
               sw temp1(r0), r3
% assigning temp1 to y
               lw r1, temp1(r0)
               sw y(r0), r1
               addi r1, r0, 0
%storing 9 into lit3
               addi r1, r0, 9
               sw lit3(r0), r1
                addi r1, r0, 0
% assigning lit3 to z
               lw r1, lit3(r0)
               sw z(r0), r1
               addi r1, r0, 0
% assigning z to q
               lw r1, z(r0)
               sw q(r0), r1
               addi r1, r0, 0
%relation z eq z
               lw r1, z(r0)
               lw r2, z(r0)
               ceq r3, r1, r2
               sw temp2(r0), r3
% starting if statment
                lw r1, temp2(r0)
                bz r1, else1
               % processing: write(x)
               lw r1, x(r0)
               % put value on stack
               sw -8(r14), r1
               % Link buffer to stack
               addi r1,r0, buf
               sw -12(r14), r1
               % convert int to string for output
               jl r15, intstr
               sw -8 (r14), r13
               % output to console
               jl r15, putstr
%storing 1 into lit4
               addi r2, r0, 1
               sw lit4(r0), r2
                addi r2, r0, 0
%storing 2 into lit5
               addi r2, r0, 2
               sw lit5(r0), r2
                addi r2, r0, 0
%relation lit4 eq lit5
               lw r2, lit4(r0)
               lw r3, lit5(r0)
               ceq r4, r2, r3
               sw temp3(r0), r4
% starting if statment
                lw r2, temp3(r0)
                bz r2, else2
%storing 10000 into lit6
               addi r3, r0, 10000
               sw lit6(r0), r3
                addi r3, r0, 0
               % processing: write(lit6)
               lw r1, lit6(r0)
               % put value on stack
               sw -8(r14), r1
               % Link buffer to stack
               addi r1,r0, buf
               sw -12(r14), r1
               % convert int to string for output
               jl r15, intstr
               sw -8 (r14), r13
               % output to console
               jl r15, putstr
               j endif2
else2%storing 1 into lit7
               addi r3, r0, 1
               sw lit7(r0), r3
                addi r3, r0, 0
               % processing: write(lit7)
               lw r1, lit7(r0)
               % put value on stack
               sw -8(r14), r1
               % Link buffer to stack
               addi r1,r0, buf
               sw -12(r14), r1
               % convert int to string for output
               jl r15, intstr
               sw -8 (r14), r13
               % output to console
               jl r15, putstr
endif2               j endif1
else1               % processing: write(y)
               lw r1, y(r0)
               % put value on stack
               sw -8(r14), r1
               % Link buffer to stack
               addi r1,r0, buf
               sw -12(r14), r1
               % convert int to string for output
               jl r15, intstr
               sw -8 (r14), r13
               % output to console
               jl r15, putstr
endif1               % processing: write(q)
               lw r1, q(r0)
               % put value on stack
               sw -8(r14), r1
               % Link buffer to stack
               addi r1,r0, buf
               sw -12(r14), r1
               % convert int to string for output
               jl r15, intstr
               sw -8 (r14), r13
               % output to console
               jl r15, putstr
               hlt

               % space for variable x
x              res 4
               % space for variable y
y              res 4
               % space for variable z
z              res 4
               % space for variable q
q              res 4
               % space for variable lit1
lit1           res 4
               % space for variable lit2
lit2           res 4
               % space for variable temp1
temp1          res 4
               % space for variable lit3
lit3           res 4
               % space for variable temp2
temp2          res 4
               % space for variable buf
buf            res 20

               % space for variable lit4
lit4           res 4
               % space for variable lit5
lit5           res 4
               % space for variable temp3
temp3          res 4
               % space for variable lit6
lit6           res 4
               % space for variable lit7
lit7           res 4
