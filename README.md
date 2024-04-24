# csc326-TP-202-4

# Additional Features [EXTRA CREDIT]
The features that we chose to implement...

## ( 1 ) Anonymous Orders *(20 points)*
### Description
We implemented a Guest functionality that allows customers to anonymously submit orders and interact with the system. As a guest, they will resume a user type most similar to that of Customer than Staff. As such, a Guest user will only be allowed to place an order, view the status of their order, and view all of the orders that they have submitted within that session.
### Extra Features
Additionally, we added a feature to the Guest view that allows guests to create a Customer account (with the current data saved -- that is, the orders that they made while in that guest session) before exiting. The automatically generated names given to guests reflect the number of guests interacting with the system. So, if one user continues as guest, they will be names guest0. If they end up creating a Customer account off of their guest session and then exit, the next user to select *Continue as Guest* will automatically be named *guest1*.

## ( 2 ) Order History *(20-30 points)*
### Description
Staff members can view the order history of orders that have been sent to them by customers (both authenticated customers and anonymous guests). This history includes both orders that have been successfully fulfilled, as well as orders that are still being processed by staff.

Customers and guests can view their order history of both orders that have been fulfilled and orders that are still being processed by staff members. Customer histories differ from that of guest histories in that customers can view all the orders they have ever submitted from their Customer account. Guests, on the other hand, can only view the history of orders that they have submitted during that current guest session. This is because they are anonymous, and their data is expected to be temporary, not long term.
### Functionality Features
Staff order history appears as a bulleted list, formatted in the following way: 
| order | customer who ordered | fulfillment status / staff who fulfilled it | pickup status (did customer pick it up yet?) |
|-----|-----|----|------|

Customer and Guest order history appears as a bulleted list, formatted in the following way:
| order | fulfillment status | pickup status (did customer pick it up yet?) |
|-----|----|------|

## ( 3 ) Privacy Policy (implemented by srschere) *(20 points)*
### Description
Privacy policy details the information that is expected to be collected from a user, the reasons for collecting that data and what it is used for or where it goes, information on potential policy changes, and data access, among other details.

### Functionality Features
We created a total of 4 privacy policies: a general privacy policy found on the home/login page, a guest privacy policy found on the guest home page, a customer privacy policy found on the customer home page, and a staff privacy policy found on the staff home page.

The general and guest privacy policies covers the following information (this will be brief listed, i.e. the privacy policy section names):
- Customer information collected
- Staff information collected
- Choice in data disclosure
- Accessing your data
- Data security
- Report concerns
- Policy changes

The Customer privacy policy covers the following information:
- Customer information we collect
- Customer information we do *not* collect
- Choice in data disclosure
- Accessing your data
- Data security
- Report concerns
- Policy changes

The Staff privacy policy covers the following information:
- Staff information we collect
- Staff information we do *not* collect
- Choice in data disclosure
- Accessing your data
- Data security
- Report concerns
- Policy changes

## ( 4 ) Security Audit *(20-35 points)*
### What Security Flaws We Found
1. Requirement was to authenticate user login, but it did not specify against storing passwords as plain-text. **Problem**: This is not a secure practice for maintaining account and platform security.
    - While authentication is crucial for a good login system and secure application that is used by both staff members and customers, it is important to securely store user passwords as well. In addition to authenticating user login attempts and restricting access to the application unless the system has validated their credentials in its user database, it is important to store the passwords as encoded hash values rather than *plain-text* passwords. 
    - **Implemented Fix**: In addition to using Spring security to implement user authentication, we also took it further and used its BCrypt password encoder security bean to encode and hash the passwords in order to store them more securely in the system. This helps keep the user protected and their account secure.
2. **Problem**: Because guests do not have authentication, there is a potential that they can accidentally access pages only customers or staff should be able to access pages
    - For instance, a guest should not be able to access the customer home page, because they are not authenticated customers and - as such - do not have access to all the same functionalities, such as being able to see their entire order history.
    - **Implemented Fix**: We created a new user type called *guest* for anonymous customers. This allowed us to simplify the functionality differences and, ultimately, separate the two types of customers from each other. We also extend guest security by allowing them to save their history by creating an authenticated customer account off of their current guest session. This then allows the guest to continue using the system as a logged in customer.
3. **Problem**: Account creation can have security risks such as weak usernames and weak passwords (simple, too short, etc).
    - **Implementation Fix**: We have implemented form validation that requires a user to enter a username and password of a certain length. Additionally, the user must submit their password correctly twice in order to successfully create their account. This ensures consistency and account security by reaffirming password strength.
4. **Problem**: If authorization is not handled properly, "correct" login authentication could allow access to the wrong pages.
    - For example, a customer may log in and correctly be authenticated as a user under a CUSTOMER role, but unless certain precautions are taken, the customer is at risk of potentially accessing a Staff-only page if authorization is not handled properly through login.
    - **Implementation Fix**: We created separate login pages that only allow authorization if certain authentication is met. For example, a user wanting to log in as customer will be redirected to a customer-specific portal, and if they enter valid staff credentials, they will be rejected because - while they were correctly authenticated as a valid staff member, they were authenticated with the correct authorization for the page.

## ( 5 ) UI Enhancements *(10-20 points)*
1. Basic form and operation validation messages
    - Colors and messages indicate to the user whether they have submitted an appropriate field value, form value, option, etc.
2. Mobile viewing compatibility
    - HTML pages are responsive to different mobile/web browser viewings. So for instance, viewing *customerlogin.html* on a computer will make everything appear wider and smaller, whereas on something like an iPhone12, that same page would appear smaller, narrower, and generally more compact, while also maintaing the original formatting, organization, and design.
