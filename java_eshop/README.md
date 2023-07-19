
Your task is to create an application server using Java with the Spring framework.

Application server  enables management of the goods, warehouse and shopping carts. Web Interface(API), and also the objects that are used to communicate with the outside world are defined in the specification and must be used to communicate through web services. Outside the web interface you can use any other objects according to your design, if you deem it appropriate.

Web interface specification that the application has to provide can be found here: https://app.swaggerhub.com/apis-docs/sk-stuba-fei-uim-oop/OOP_Zadanie_3/1.0.0

If the specification states that a 404 code should be returned, and the description does not say when, it is necessary to return if given ID does not exist in the system.

### System description

A detailed description of each operation is given in the API specification.

The system allows adding and removing products from the store's assortment. Also, it allows editing of existing products as well as increase the status of products in stock.

The system allows you to create and delete orders. It is possible to add products in orders in the specified quantity (if there is enough product in stock). The system also allows the payment of unpaid orders. It is not possible to add additional items to paid orders.

## Automatic tests
Project contains automatic tests. Tests **DO NOT** run automatically on git push. If you want to verify how many of your implementations pass the tests, you must run them yourself. Tests can be run in 2 ways:

* using Maven, by launching lifecycle (side menu> maven> project> lifecycle> test)
* by running tests directly from the class that contains them (located in rc/test/sk/.../oop/assignment3/Assignment3Tests.java))
