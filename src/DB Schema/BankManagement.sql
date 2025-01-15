create database bankManagement;
use bankManagement;

create table Customer(
customerId int primary key auto_increment,
name varchar(255),
email varchar(255),
password varchar(255),
panNo varchar(255),
phone varchar(255),
address varchar(255),
index(panNo)
)auto_increment = 101;

create table Account(
accountId int primary key auto_increment,
customerId int,
panNo varchar(255),
accountType enum('Savings', 'Current'),
balance int,
constraint fk_account foreign key(customerId) references Customer(customerId),
constraint fk2_account foreign key(panNo) references Customer(panNo)
)auto_increment=1001;

create table Transaction(
transactionId int primary key auto_increment,
accountId int,
transactionType enum('Deposit','Withdrawal','Transfer','Credit'),
toTransfer varchar(255),
amount int,
remainingBalance int,
transactionDate timestamp default current_timestamp,
constraint fk_Transaction foreign key(accountID) references Account(accountID)
)auto_increment=7001;

create table Loan(
loanId int primary key auto_increment,
customerId int,
loanType enum('Personal','Home','Education'),
amount int,
interestRate decimal(3,1),
status enum('pending','Approved','Rejected') default 'Pending',
constraint fk_loan foreign key(customerId) references Customer(customerId)
)auto_increment=401;


delimiter //
create procedure getAllLoanDetail()
begin
select  l.loanId,c.customerId,c.name,a.accountId,a.accountType, a.balance,
		l.loanType, l.amount as loanAmount, l.interestRate, l.status
        from customer c join account a on c.customerId=a.customerId
        join loan l on c.customerId=l.customerId where l.status not in("Approved","Rejected");
end//
delimiter ;

delimiter //
create procedure getAllCustomer()
begin
select
    c.customerId,c.name,c.email,c.phone,c.address,
    a.accountId,a.accountType,a.balance,
	COALESCE(l.loanId, 'NIL') AS loanId,
    COALESCE(l.loanType, 'NIL') AS loanType,
    COALESCE(l.amount, 'NIL') AS loanAmount,
    COALESCE(l.interestRate, 'NIL') AS interestRate,
    COALESCE(l.status, 'NIL') AS loanStatus from Customer c 
left join  Account a on c.customerId = a.customerId
left join Loan l on c.customerId = l.customerId;
end//
delimiter ;


create table Admin(
adminId int primary key auto_increment,
userName varchar(255),
password varchar(255)
)auto_increment=101101;
insert into admin(userName,password) values("admin1","Adminpass@1");


delimiter //
create procedure generateFinancialReport()
begin
select c.customerId,c.name,
a.accountId,a.balance,

coalesce(case when l.pending_loans=0 or l.pending_loans is null then '-'
		 else l.pending_loans end
         ,'-') as pending_loans,
coalesce(case when l.approved_loans=0 or l.approved_loans is null then '-'
		 else l.approved_loans end 
         ,'-') as approved_loans,


coalesce(case when t.totalDeposit=0 or t.totalDeposit is null then '-'
		 else t.totalDeposit end
         ,'-') as totalDeposit,
coalesce(case when t.totalWithdrawal=0 or t.totalWithdrawal is null then '-'
		 else t.totalWithdrawal end,'-')
as totalWithdrawal,
coalesce(case when t.totalSent=0 or t.totalSent is null then '-'
		 else t.totalSent end
         ,'-')as totalSent,
coalesce(case when t.totalReceived=0 or t.totalReceived is null then '-'
		 else t.totalReceived end
         ,'-')as totalReceived from customer c 
left join account a on c.customerId=a.customerId

left join(
select customerId,
sum(case when status='pending' then amount else 0 end) as pending_loans,
sum(case when status='approved' then amount else 0 end) as approved_loans
 from loan group by customerId
 ) l on l.customerId=c.customerId
 
left join(
select a.customerId,a.accountId,
sum(case when t.transactionType='Deposit' then t.amount else 0 end)as totalDeposit,
sum(case when t.transactionType='Withdrawal' then t.amount else 0 end )as totalWithdrawal,
sum(case when t.transactionType='Transfer' then t.amount else 0 end )as totalSent,
sum(case when t.transactionType='Credit' then t.amount else 0 end )as totalReceived
from account a 
left join transaction t 
on a.accountId=t.accountId
group by a.customerId,a.accountId) t on t.accountId=a.accountId 
order by a.customerId,a.accountId;
end //
delimiter ;