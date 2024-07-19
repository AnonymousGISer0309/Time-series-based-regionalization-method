# Time-series-based-regionalization-method

## 1. Data and code
This project mainly includes a regionalization algorithm written in Java and POI data containing Shanghai business hours information.

## 2. Algorithm Introduction
First, the geographic attribute data is converted into temporal OD with location, and then its probability density in different temporal periods is calculated to obtain a time series curve that is continuous in time.

![image](https://github.com/AnonymousGISer0309/Time-series-based-regionalization-method/blob/main/Construction%20of%20temporal%20OD%20flow.png)

On this basis, regionalization operation is performed on the study area according to the temporal attributes with geographical location.

![image](https://github.com/AnonymousGISer0309/Time-series-based-regionalization-method/blob/main/Regionalization%20workflow.png)

## 3. Methodological contribution

Previous studies have defined and used time-series data based on two characteristics. First, native time-series data such as GDP, population, and precipitation are commonly used. These data work well in large-scale studies but are often inadequate for small-scale studies within cities. Second, the spatial features of time-series data in geographical issues are frequently disregarded, resulting in relatively scarce research on the spatial correlation, heterogeneity, and clustering of time-series data. To address this, a new method for constructing time-series data using the attributes of POIs was explored. In this manner, each point can have a time-series, and geographic units of any scale can generate corresponding time-series, providing a data basis for multi-scale spatial time-series data research.

![image](https://github.com/AnonymousGISer0309/Time-series-based-regionalization-method/blob/main/Methodological%20contributions.png)
