# CQRS Architecture 
<img width="2004" alt="image" src="https://github.com/renxinx/ScalableTwinder/assets/90580890/fa6aa7eb-f928-4cfa-9d5c-8358c59a5d03">

# Assignment 1 Report

## Description of Client Design

### Major Classes

The `Main` class is responsible for running multiple threads, with each thread executing 2500 requests by invoking the `SwipeRequests` class.

#### Main Class

1. Main class to run the multiple threads, which contains 200 threads, each thread is going to run 2500 requests calling SwipeRequests class;
![image](https://github.com/renxinx/ScalableTwinder/assets/90580890/d73e0ced-4b0a-493c-a996-f8cc65ec0924)

2. Using AtomicInteger succ and fail to track successful and failed requests in total;
3. Using BlockingDeque requestRecords, responseTimes, throughputList to track performance indicators of the client;
![image](https://github.com/renxinx/ScalableTwinder/assets/90580890/1966219c-e4ac-49fc-9597-9574a352d422)
4. Print out the number of successful requests sent, number of unsuccessful requests, the total run time (wall time) for all threads to complete, the total throughput in requests per second, the mean response time, the median response time, the p99 response time, the min response time and the max response time.
![image](https://github.com/renxinx/ScalableTwinder/assets/90580890/3f1daff9-75ba-48c1-8472-559762edeb53)


#### SwipeRequests class:

1. Method sendRequests: give each request 5 try times;
![image](https://github.com/renxinx/ScalableTwinder/assets/90580890/e72c5780-848d-4e02-84c6-7b661ac63e37)
2. Method run: generate random data to Swiper, Swipee, Comment and LeftorRight into SwipeDetails body. If the request sent successfully, increase the successful requests amount, otherwise increase the failed requests amount;
![image](https://github.com/renxinx/ScalableTwinder/assets/90580890/e920fa30-2e94-4e1b-8e5c-fbf4a43d3062)

## Client Part1
![image](https://github.com/renxinx/ScalableTwinder/assets/90580890/67347f62-337e-40a6-be1b-3ff37d29448d)
number of threads used: 200
Little’s Law throughput predictions: 15386 requests per second

## Client Part2
![image](https://github.com/renxinx/ScalableTwinder/assets/90580890/850200d8-0ff5-4802-8e3d-cda54de102d6)
The difference in response time might be because threads need the same locks but obtain them in a different order, which causes deadlocks.

# Assignment 2 Report

## Description of server design

### TwinderServer
1. Validate the request;
2. Validate url information;
3. Organize the request data into swipe format and pack it into payload message;
4. Send the message the rabbitmq queue.
<img width="365" alt="image" src="https://github.com/renxinx/ScalableTwinder/assets/90580890/8d94bb80-0f9a-4e97-9575-1bdf9bac23ae">

### Consumer1
Given a user id, return the number of likes and dislikes this user has swiped.
![image](https://github.com/renxinx/ScalableTwinder/assets/90580890/bc89f3c5-bbd7-4396-8cf8-fb68d6a16ec6)

### Consumer2
Given a user id, return a list of 100 users maximum who the user has swiped right on. These are potential matches.
![image](https://github.com/renxinx/ScalableTwinder/assets/90580890/d07fc9ad-8e4e-48d6-843d-e6da7f8d6376)

## Test Run Results

### Without load balancing
![image](https://github.com/renxinx/ScalableTwinder/assets/90580890/21da1dba-e76b-4af4-9c10-3592c401cac9)
- **Consumer1**
  ![image](https://github.com/renxinx/ScalableTwinder/assets/90580890/e6d9416d-79e3-41ba-b6ee-36ca70304a02)
  
- **Consumer2**
  ![image](https://github.com/renxinx/ScalableTwinder/assets/90580890/a51a3257-5532-4720-9125-07544bd50dd1)

### With load balancing
![image](https://github.com/renxinx/ScalableTwinder/assets/90580890/37d7d351-4d2f-4392-899d-7648e71413a2)
- **Consumer1**
  ![image](https://github.com/renxinx/ScalableTwinder/assets/90580890/0a5c603b-69d5-4ddc-8094-2dc797f909ec)
  
- **Consumer2**
  ![image](https://github.com/renxinx/ScalableTwinder/assets/90580890/abddd30c-dcb9-4d24-a118-714cf47f34f5)


# Assignment 3 Report

## Description of server design

### TwinderServer
1. Servlet: doPost() implementation; Organize the request data into swipe format and pack it into payload messages; Send the message the rabbitmq queue.
2. MatchesServlet: doGet() implementation.
3. StatsServlet: doGet() implementation. 
![image](https://github.com/renxinx/ScalableTwinder/assets/90580890/51e3e62f-4652-4ed9-8b41-0a68f4e5eda5)

### TempStore
I used the RabbitMQ for message broker, aka Tempstore.

### Consumer1
1. Consume message from queue and process it into expected form.
2. Write data into Stats table from AWS RDS.
![image](https://github.com/renxinx/ScalableTwinder/assets/90580890/f71d33ed-ec20-4518-86e5-8452d20b1030)

### Consumer2
1. Consume message from queue and process it into expected form.
2. Write data into Matches table from AWS RDS.
![image](https://github.com/renxinx/ScalableTwinder/assets/90580890/547bea5a-a4d0-4f1d-8d94-a0c0f59dfa9c)

### Client
generates 5 GET requests with randomly generated data every second and records the latencies.
![image](https://github.com/renxinx/ScalableTwinder/assets/90580890/6c0f486f-0a55-4472-bfa4-81415c245c7e)

### Database
![image](https://github.com/renxinx/ScalableTwinder/assets/90580890/4d56a968-2843-44ae-84f3-e418433b126d)
![image](https://github.com/renxinx/ScalableTwinder/assets/90580890/2c5dec13-35c9-445b-9323-fbb9c0541074)

## Client test run screenshots
![image](https://github.com/renxinx/ScalableTwinder/assets/90580890/8f80b71c-9dcb-4884-8263-8fd8aecb72a8)

Comparison with assignment2:
![image](https://github.com/renxinx/ScalableTwinder/assets/90580890/3e892ff4-b48e-4e2b-ac70-72c67b7265ca)

Technically the performance should be slower than last time, but it shows that this time the throughput is better. Possible reasons could be network connection, and aws traffic jam.
