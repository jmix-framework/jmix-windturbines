### 1. Introduction

While mobile applications for line-of-business (LOB) applications have the potential to significantly enhance internal
business operations, their adoption for custom software solutions has been relatively limited, particularly among small
and medium-sized businesses. Traditionally, the high investment costs associated with developing dedicated native apps
have been a significant barrier, especially when supporting multiple platforms. This has made mobile browser
applications an attractive solution for keeping costs manageable. However, there is a growing need to extend business
processes directly to end-users in the field, making mobile solutions increasingly crucial for internal use.

Employees need tools that allow them to access live systems and perform their tasks in real-time. In the past,
businesses often had to rely on interim solutions due to the lack of direct exposure to core or line-of-business
systems. This often meant using unstructured data entry methods, which then required manual post-processing to integrate
with the main business systems. Such methods were inefficient and prone to errors, as they did not allow for real-time
interaction with the core systems.

There are numerous use cases for mobile applications in the business context. These include field service workers, sales
teams, and third-party partners who need access to the system. Mobile tools can also be vital for customers who require
real-time interaction with company services. By providing such tools, companies can ensure that work is done more
efficiently and accurately, improving overall productivity and communication.

In previous versions of Jmix, the user interface was primarily optimized for desktop use. Consequently, creating mobile
solutions often required developing either a native app or a mobile web app using different technologies, such as
JavaScript single-page applications (SPAs), while leveraging Jmix only as a backend. Although it was possible to create
mobile applications this way, it was not the common approach because mobile support was not a first-class citizen in
Jmix.

However, with the release of Jmix 2 and the transition to Vaadin 24, there has been a significant improvement in
responsive design and mobile application capabilities. This technical advancement now makes mobile support a first-class
citizen in Jmix, enabling the core value add of speedy delivery, a hallmark of Jmix business applications, to be applied
to mobile business applications as well. The same efficiency gains and cost-effective development processes can now be
expected for mobile solutions. This means that businesses can now expose their core business processes and systems
directly to end-users in the field with much less effort. End-users can then directly interact with these systems to
perform their specific business tasks.

Moreover, with the proliferation of mobile devices and the ubiquity of mobile data plans, utilizing mobile technology
has become seamless and unobtrusive. It is now a standard practice, making it easier for businesses to implement mobile
solutions without significant barriers.

In this blog post, you will learn how Jmix leverages these technologies. We will present an example project that
showcases the implementation of such a mobile application.