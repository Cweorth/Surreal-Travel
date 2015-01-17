This file concerns user roles and their permissions in the Surreal-Travel application.

#### 1. General
  1. rights that are not permitted are denied,
  2. rights in points ```3``` to ```6``` are **permitted** unless specified otherwise,

#### 2. Roles hierarchy:
  1.  ```ADMIN``` inherits ```STAFF``` rights,
  2. ```STAFF``` inherits ```USER``` rights,
  3. Guest (unauthenticated user) rights are **not** inherited,
  4. rights specified in a higher role have higher priority than inherited rights,

#### 3. Guest rights (no authentication):
  1. list excursions and trips,
  2. login,
  3. create an account (with or without customer) with ```USER``` permissions,

#### 4. ```USER``` rights
  1. logout,
  2. list excursions and trips,
  3. change password of **own** account,
  4. remove **own** account,
  5. if has a ```Customer``` attached,
    1. edit the attached customer,
    2. create reservation,
    3. list and remove **own** reservations,

#### 5. ```STAFF``` rights
  1. create and edit excursion
     1. remove excursion if **not referenced** by any trip,
  2. create and edit trip
      1. remove trip if **not referenced** by any reservation,
  3. list, edit and delete reservations,

#### 6. ```ADMIN``` rights
  1. list customers and accounts,
  2. edit customer,
  3. remove customer if **not referenced** by any account or reservation,
  3. edit account
      1. change user rights except for the ```root``` account,
  4. remove account unless it is the ```root``` account,
      1. **deny** ```4.4``` (remove own account) to ```root```,
  5. create, edit and remove customer **not referenced** by any account,

#### 7. Default accounts

| no. | username    | rights      | default password | customer    |
|-----|-------------|-------------|------------------|-------------|
| 1   | ```root```  | ```ADMIN``` | ```root```       | *none*      |
| 2   | ```pa165``` | ```ADMIN``` | ```pa165```      | ```pa165``` |
| 3   | ```rest```  | ```STAFF``` | ```rest```       | *none*      |
