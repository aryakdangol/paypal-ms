# PayPal Integration Task

This project integrates paypal api to create, approve, cancel and capture the order


## Project Setup

### Installation
- Install docker on local system
- Clone the repository and checkout to development branch
- Add .env file the content are shared in environment setup section
- Run ./gradlew clean build 
- Run docker compose up --build

### Environment Setup
The project requires env variables as some secrets are injected through it.
After creating the .env file mentioned in step 3 add following variables:
- DATABASE_PASSWORD
- DATABASE_USERNAME
- JWT_EXPIRY_TIME
- JWT_SECRET
- PAYPAL_CLIENT_ID
- PAYPAL_CLIENT_SECRET

#### Paypal Account Setup
Setting up variables 1-4 is fairly straight forward.     
For Paypal client id and client secret we can setup the sandbox environment through following steps:
- Login to [paypal developer portal](https://developer.paypal.com/dashboard/applications/sandbox)
- Setup your developer account and then create an Application to get the Client Secret and Client ID.
- Use ths credentials in the PAYPAL_CLIENT_ID and PAYPAL_CLIENT_SECRET fields in .env file. 
- Then create two sandbox accounts one for business one for personal. Paypal will create a sandbox business account by default while creating the app.  
- Register IPN on your sandbox paypal business account by logging in using the sandbox credentials provided in the App section.

Since, Paypal API returns order status and IPN via callback we also need to tunnel our gateway port to be accesible by Paypal.  
Install ngrok to tunnel localhost to an accesible url using command ngrok http 8080. The details are here: [ngrok-reference](https://ngrok.com/docs/getting-started/)  


### Usage

To use the app please download the postman collection freom: [api-collection](https://www.postman.com/spacecraft-geoscientist-54000230/public-workspace/collection/ttvtn2u/payments?action=share&creator=17615029).
The flow is as follows:
- Use auth/signup to register a user
- Login the user using auth/login
- Use the jwt generated in previous step to create an order using /api/payments/createOrder
- On createOrder api add the ngrok host under successUrl and cancelUrl.
- You can view your created orders using /api/payments/getAllOrders endpoint
- To view the details of order use /api/payments/viewOrder/{orderId} where orderId is the paypal generated order id.
- To approve the order simply follow the approvalLink from the viewOrder or createOrder api. Please note that to approve you must be sign into a personal account
- Once approved the transaction is processed and the order status is updated.

All incoming request should be passed through gateway service.




