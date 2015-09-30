# ml_onlinenewspopularity

This project perfoms machine learning on a give training data. It can produce both the closed-form solution and the gradient descent solution

The training data should be provided as csv file. The first line of the file should be the names of the features that will be provided followed by the name of the target column.
The successive lines should contain numeric data for the respective features and the target column, separated by commas.

There are 3 main modules. 
1. OnlineNewsPopularity.java: This is the class contains main() and interacts with the user.
2. DataFormatter.java       : This class does all the file IO and formatting data into test and train sets.
3. LinearRegression classes : Theses classes perform various linear regression techinques on the training data. The also have a functionality to give predictions based on trained models.
    The abstract class LinearRegression lays down the framework for writing any functional linear regression class.
    ClosedFormSolution and GradientDescent extend LinearRegression.

In order to find the closed-form solution, simply create an object of ClosedFormSolution and run doLinearRegression().
Similarly, create an object of GradientDescent and run doLinearRegression() to run gradient descent on the training data.

GradientDescent, additionally performs, model testing using k-fold cross validation.

Once the models have been trained, they can be used to predict data using predict().
