create table users
(
    user_id       serial
        primary key,
    username      varchar(255) not null,
    email         varchar(255) not null
        unique,
    password_hash varchar(255) not null,
    created_at    timestamp with time zone default CURRENT_TIMESTAMP,
    updated_at    timestamp with time zone default CURRENT_TIMESTAMP
);

comment on table users is 'Table for storing user information, including login credentials and profile details.';

comment on column users.user_id is 'Unique identifier for each user; primary key.';

comment on column users.username is 'Username chosen by the user; unique and used for identification within the app.';

comment on column users.email is 'User''s email address; used for account verification and communication; unique across all users.';

comment on column users.password_hash is 'Hash of the user''s password for secure authentication.';

comment on column users.created_at is 'Timestamp indicating when the user account was created.';

comment on column users.updated_at is 'Timestamp of the last update made to the user''s account information.';

create table incomes
(
    income_id   serial
        primary key,
    amount      numeric(10, 2) default 0 not null,
    source      varchar(255)             not null,
    income_date date                     not null,
    description varchar,
    user_id     integer
        references users
);

comment on table incomes is 'Table for recording various income sources for users.';

comment on column incomes.income_id is 'Unique identifier for each income record; primary key.';

comment on column incomes.amount is 'Amount of income received.';

comment on column incomes.source is 'Source of income (e.g., salary, rental income).';

comment on column incomes.income_date is 'Date when the income was received.';

comment on column incomes.description is 'Optional description providing more details about the income source.';

comment on column incomes.user_id is 'Identifier of the user receiving the income; foreign key referencing users table.';

create table expenses
(
    expense_id   serial
        primary key,
    amount       numeric(10, 2) not null,
    category     varchar(255)   not null,
    expense_date date           not null,
    description  text,
    user_id      integer
        references users
);

comment on table expenses is 'Table for recording the various expenses incurred by users.';

comment on column expenses.expense_id is 'Unique identifier for each expense record; primary key.';

comment on column expenses.amount is 'Monetary value of the expense.';

comment on column expenses.category is 'Category to which the expense belongs (e.g., groceries, utilities).';

comment on column expenses.expense_date is 'Date when the expense was incurred.';

comment on column expenses.description is 'Optional text description providing more details about the expense.';

comment on column expenses.user_id is 'Identifier of the user who incurred the expense; foreign key referencing users table.';

create table savings_goals
(
    saving_goal_id   serial
        primary key,
    saving_goal_name varchar(255)   not null,
    target_amount    numeric(10, 2) not null,
    current_amount   numeric(10, 2) not null,
    start_date       date           not null,
    end_date         date,
    user_id          integer
        references users
);

comment on table savings_goals is 'Table for tracking savings goals set by users.';

comment on column savings_goals.saving_goal_id is 'Unique identifier for each savings goal; primary key.';

comment on column savings_goals.saving_goal_name is 'Name of the savings goal (e.g., vacation, emergency fund).';

comment on column savings_goals.target_amount is 'Target amount to be saved for the goal.';

comment on column savings_goals.current_amount is 'Current amount saved towards the goal.';

comment on column savings_goals.start_date is 'Start date for the savings goal.';

comment on column savings_goals.end_date is 'End date or target date to achieve the savings goal.';

comment on column savings_goals.user_id is 'Identifier of the user who set the savings goal; foreign key referencing users table.';

create table budgets
(
    budget_id    serial
        primary key,
    month        integer        not null
        constraint budgets_month_check
            check ((month >= 1) AND (month <= 12)),
    year         integer        not null
        constraint budgets_year_check
            check (year >= 1900),
    total_budget numeric(10, 2) not null,
    user_id      integer
        references users
);

comment on table budgets is 'Table for managing the monthly budgets set by users.';

comment on column budgets.budget_id is 'Unique identifier for each budget record; primary key.';

comment on column budgets.month is 'Month for which the budget is set.';

comment on column budgets.year is 'Year for which the budget is set.';

comment on column budgets.total_budget is 'Total budget amount set by the user for the specified month and year.';

comment on column budgets.user_id is 'Identifier of the user to whom the budget belongs; foreign key referencing users table.';

create table budget_details
(
    budget_details_id serial
        primary key,
    allocated_amount  numeric(10, 2) not null,
    category          varchar(255)   not null,
    budget_id         integer
        references budgets
);

comment on table budget_details is 'Table for storing detailed budget allocations for different categories in user budgets.';

comment on column budget_details.budget_details_id is 'Unique identifier for each budget detail record; primary key.';

comment on column budget_details.allocated_amount is 'Amount allocated to the specific category within the budget.';

comment on column budget_details.category is 'Category of the budget allocation (e.g., groceries, entertainment).';

comment on column budget_details.budget_id is 'Identifier of the associated budget; foreign key referencing budgets table.';

