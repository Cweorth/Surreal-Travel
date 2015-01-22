This file concerns user roles and their permissions in the Surreal-Travel application.

#### 1. General
  1. rights that are not permitted are denied,
  2. rights in points ```3``` to ```6``` are **permitted** unless specified otherwise,

#### 2. Roles hierarchy:
  1. inheritance relation ```USER``` < ```STAFF``` < ```ADMIN``` < ```ROOT```,
  2. guest rights (eg. unauthenticated user) are **not** inherited,
  3. rights explicitly specified in a higher role have higher priority than inherited rights,

#### 3. Guest rights (unauthenticated user):
  1. login,
  2. list excursions and trips,
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
  1. create and edit excursions
     1. remove excursion if **not referenced** by any trip,
  2. create and edit trips
     1. remove trip if **not referenced** by any reservation,
  3. list, edit and delete reservations,

#### 6. ```ADMIN``` rights
  1. list customers and accounts,
  2. edit customers,
  3. remove customer if **not referenced** by any account or reservation,
  4. edit any account without ```ROOT``` role
     1. change password,
     2. grant / revoke roles except ```UserRole.ROLE_ROOT```,
     3. no password required for ```USER``` and ```STAFF```,
     4. administrator's password required for ```ADMIN```
  5. remove any account without ```ROOT``` role
     1. password requirements same as in ```6.3```,
  6. create, edit and remove customers **not referenced** by any account,

#### 7. ```ROOT``` rights (exceptions)
  1. **deny** §*4.4* (removing own account),
  2. change password of *own* account

#### 8. Table of rights applied to entities

| Role        | Account    | Customer   | Excursion  | Trip       | Reservation |
|-------------|------------|------------|------------|------------|-------------|
| *guest*     | ```C---``` | ```C---``` | ```--L-``` | ```--L-``` | ```----```  |
| ```USER```  | ```-e-d``` | ```-e-d``` | ```--L-``` | ```--L-``` | ```P--d```  |
| ```STAFF``` | ```-e-d``` | ```-e-d``` | ```CELR``` | ```CELR``` | ```PELD```  |
| ```ADMIN``` | ```CELD``` | ```CELR``` | ```CELR``` | ```CELR``` | ```PELD```  |
| ```ROOT```  | ```CELD``` | ```CELR``` | ```CELR``` | ```CELR``` | ```PELD```  |

Position system: ```CELD``` where
   * ```C``` - create, ```E``` - edit, ```L``` - list, ```D``` - delete, ```-``` - not allowed, 
   * ```R``` - restricted delete (not referenced entity), ```P``` restricted create (must be a customer),
   * lowercase = operation allowed only on an entity owned by the user,
   * uppercase = operation allowed on all entities (in case of accounts only on **LOWER or SAME** role),

###  9.  Details of Edit and Delete operations on accounts

| Role        | **SELF**   | ```USER``` | ```STAFF``` | ```ADMIN``` |
|-------------|------------|------------|-------------|-------------|
| *guest*     | ```----``` | ```----``` | ```----```  | ```----```  |
| ```USER```  | ```*c-d``` | ```----``` | ```----```  | ```----```  |
| ```STAFF``` | ```*c-d``` | ```----``` | ```----```  | ```----```  |
| ```ADMIN``` | ```*cpd``` | ```-cpd``` | ```-cpd```  | ```*cpd```  |
| ```ROOT```  | ```*c--``` | ```-cpd``` | ```-cpd```  | ```*cpd```  |

The columns specify the *target* account, where *the first match* from the left applies.

Position system: ```*cdp``` where
   * ```*``` - operation requires password verification, ```c``` - change password, ```p``` - change permissions, ```d``` - delete

#### 10. Default accounts

| no. | username    | rights      | default password | customer    |
|-----|-------------|-------------|------------------|-------------|
| 1   | ```root```  | ```ADMIN``` | ```root```       | *none*      |
| 2   | ```pa165``` | ```ADMIN``` | ```pa165```      | ```pa165``` |
| 3   | ```rest```  | ```STAFF``` | ```rest```       | *none*      |
