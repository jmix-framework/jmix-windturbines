<p align="center">
  <img src="https://github.com/jmix-framework/jmix-windturbines/blob/main/img/logo-blue.svg?raw=true"/>
</p>

# Jmix Wind Turbines

Jmix Wind Turbines is a mobile example application built with [Jmix](https://jmix.io/) - a full-stack framework for business applications. It showcases the operations of a maintenance company responsible for inspecting and maintaining wind turbines at a wind farm.

![Jmix Wind Turbines - Overview](img/1-overview.png)

The online demo of the Jmix Wind Turbines application is available at <https://demo.jmix.io/windturbines>.

### Table of Content

- [About the Example](#about-the-example)
    * [Technical Details](#technical-details)
    * [Running Locally](#running-locally)
- [Application Overview](#application-overview)
    * [Inspections](#tasks)
        + [Viewing Inspections](#viewing-tasks)
    * [Wind Turbine Management](#wind-turbine-management)
        + [Search and Details](#search-and-details)
    * [Inspections](#inspections)
        + [Annual Inspection](#annual-inspection)
        + [Repair](#repair)

## TODO

* https://github.com/jmix-framework/jmix/issues/3469
* 

## About the Example

The Jmix Wind Turbines example application demonstrates the mobile capabilities Jmix provides for application developers. Compared to the [Jmix Petclinic](https://github.com/jmix-framework/jmix-petclinic-2) example application, the Wind Turbines application is focuses on a mobile first view of the application.

### Technical Details

Jmix Wind Turbines runs using:

* Jmix 2
* Spring Boot 3
* Java 21

### Running Locally

You can run the containerized application locally as follows:

- Make sure you have Docker installed
- Download [docker-compose.yml](https://github.com/jmix-framework/jmix-windturbines/blob/main/docker-compose.yml) file to a local folder
- Open a terminal in that directory and run
    ```shell
    docker-compose up
    ```

## Application Overview

Jmix Wind Turbines is an application that manages the inspection and maintenance of wind turbines at a wind farm. The application includes the following key functionalities:

### Inspections

#### Viewing Inspections

The application shows a list of all pending inspection and maintenance tasks, categorized under different tabs for easier management. This allows technicians to efficiently manage their workload and prioritize tasks.

#### Performing Inspections

Technicians can perform inspections to ensure wind turbines are functioning correctly and safely. The process includes:

- **Inspection Details**: Enter or confirm basic information such as the inspection date, technician's name, and type of inspection.
- **Checklist**: Complete a series of questions to ensure all critical aspects of the turbine are checked.
- **Findings**: Document any issues discovered during the inspection.
- **Recommendations**: Provide recommendations to address identified issues.
- **Status Update**: Update the status of the inspection task from "in progress" to "completed".
- **Signature Capture**: Capture a digital signature to confirm the completion of the inspection task.

This streamlined workflow helps maintain the safety and performance of wind turbines through detailed inspections and timely repairs.


### Wind Turbine Management

Technicians can search for specific wind turbines by ID, location, or manufacturer using a single search input field. Detailed information about each turbine is available, including manufacturer details, installation date, last maintenance date, and operational status. The list and details are read-only, ensuring data integrity as the information is maintained by the back office.

## Mobile Features

The Jmix Wind Turbines application is designed with mobile usage in mind, offering several features to enhance the experience on mobile devices:

- **Responsive Views**: The application’s views are responsive, ensuring they function well on both mobile phones and tablets. This responsiveness provides a seamless user experience across different screen sizes.
- **Optimized UI Components**: Utilizes dedicated UI components from Vaadin 24, such as dropdowns that are optimized for use on mobile phones and tablets. These components enhance usability and interaction on smaller screens.
- **PWA Support**: The application supports Progressive Web App (PWA) functionality. This allows users to install the application on their mobile devices via the browser, making it accessible as a quasi-native application. Once installed, the application can be launched directly from the home screen or desktop.
- **Mobile-First Design**: The application is primarily designed for mobile use, meaning it is not intended for primary use on desktop computers. This ensures that all features and functionalities are tailored to mobile device capabilities, providing an optimized mobile experience.
