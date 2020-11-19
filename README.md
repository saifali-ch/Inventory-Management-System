# Inventory Management System
A software complying with many design rules, principles, patterns, and anti-patterns. With a fully Relational Database Design.


# Features
- ### Pixel Perfect UI Customized with CSS.
    Everything you will see in the UI is hand-customized with CSS. Which makes UI look elegant.

- ### Highly Extensible and Scalable.
    The software is built focusing on Design Rules, Design Patterns and Anti-patterns and **SOLID** software design principles.

- ### Desing Patterns.
    I have implemented many design patterns where necessary. Including Singleton, Observable, and MVC.
- ### Caching
    I have used caching of date to reduce the overhead of fetching data over and over again from the database. The data is loaded only once from the database, then a local copy of the data is created. After that, if we need data for other parts of the UI, we can simply use data from the local copy instead of fetching it from the database again. So, it makes UI highly responsive.



# Tech Stack

**Database:** OracleXE 11g with OJDBC6 Connector

**IDEs:** IntelliJ IDEA Ultimate and DataGrip Ultimate Edition

**Languages:** Java 11, JavaFX 13, SQL

**Libraries:** ControlsFX 11.0.2, AnimationFX 1.2.1, JFoenix 9.0.9


# How to Build & Run?
## Step 1: Database Installation and Table Creation
1. Install OracleXE 11g.
2. Search **Run SQL Command Line** and Run.
3. Type the following commands:
```SQL
Alter User HR Account Unlock;
Alter User HR Identified By hr;
```
4. Add Oracle Data Source in IntelliJ IDEA, Open Query Console, and run all the queries provided in **/src/database/create.sql** file.

## Step 2: Add Libraries to Module
1. Open IntelliJ IDEA, press **CTRL+ALT+SHIFT+S** to open Project Settings.
2. Goto Global Libraries section, click the add button and add all the libraries provided in **/src/lib/** folder one by one.
  
## Step 3: Build & Run
You are all done now, just click **Run** button.
