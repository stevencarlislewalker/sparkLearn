library(magrittr)

df <- createDataFrame(sqlContext, faithful)
head(df, 10L)
printSchema(df)

select(df, "waiting") %>% head

df <- createDataFrame(sqlContext, iris)
modSpark <- glm(Sepal_Length ~ Sepal_Width + Species, data = df,   family = "gaussian")
modR     <- glm(Sepal.Length ~ Sepal.Width + Species, data = iris, family = "gaussian")

coef.PipelineModel <- function(object, ...) {
    unlist(summary(object)$coefficients[, "Estimate"])
}

coef(modSpark)
coef(modR)

summary(modSpark)
summary(modR)

logLik(modSpark)
logLik(modR)

AIC(modSpark)
AIC(modR)

drop1(modSpark)

