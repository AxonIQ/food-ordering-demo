# Part 5 - Connecting the UI

This part of the guide the Food Ordering application will be connected to the UI.
To that end, REST endpoints have been introduced in a dedicated package.

As we follow the [CQRS](https://axoniq.io/resources/cqrs) paradigm, we segregate the Command Model from our Query Model.
Thus, we have dedicated Command and Query messages which they handle respectively.

In this guide, we will introduce how you can dispatch Command and Query Messages by using the `CommandGateway`
 and `QueryGateway` within for example a REST Controller.

The video relating to this part of the guide can be found [here](https://www.youtube.com/watch?v=lxonQnu1txQ).
