
library(Rmisc)
library("tidyverse")
library("ggplot2")

x <- read.csv("onemax_stats.csv")

# 1a
x_gen_stats <- x %>% 
	group_by(R, G) %>% 
	summarize(bestF = max(F), avgF = mean(F), stdF = sd(F), CI = (t.test(F)$"conf.int"[2] - t.test(F)$"conf.int"[1])/2)
	
# 1b
x_run_stats <- x_gen_stats %>% 
	group_by(R) %>% 
	summarize(bestFit = max(bestF), avgF = mean(bestF), stdF = sd(bestF), CI = (t.test(bestF)$"conf.int"[2] - t.test(bestF)$"conf.int"[1])/2)
	
# ------------------------------------------

x_overall_stats <- x_run_stats %>% 
	summarize(bestFitness = max(bestFit), avgF = mean(bestFit), stdF = sd(bestFit), CI = (t.test(bestFit)$"conf.int"[2] - t.test(bestFit)$"conf.int"[1])/2)
	
x_overall <- x %>% 
	summarize(bestFitness = max(F), avgF = mean(F), stdF = sd(F), CI = (t.test(F)$"conf.int"[2] - t.test(F)$"conf.int"[1])/2)

# ------------------------------------------
	
# 1c
x[x$F == 200,]




# 1. a)


x_avg <- x_gen_stats %>% 
	group_by(G) %>% 
	summarize(avgBF = mean(bestF), stdBF = sd(bestF), BCI = (t.test(bestF)$"conf.int"[2] - t.test(bestF)$"conf.int"[1])/2,
				avgAF = mean(avgF), stdAF = sd(avgF), ACI = (t.test(avgF)$"conf.int"[2] - t.test(avgF)$"conf.int"[1])/2)

ggplot(x_avg, aes(x=G)) +
	geom_line(aes(y=avgBF, color="black")) + 
    geom_errorbar(aes(ymin=avgBF-BCI, ymax=avgBF+BCI), color="#ff2e2e", size=0.3) +
    geom_point(aes(y=avgBF, color="black"), size = 0.2, stroke = 0) +
	geom_line(aes(y=avgAF, color="blue")) + 
    geom_errorbar(aes(ymin=avgAF-ACI, ymax=avgAF+ACI), color="#ff2e2e", size=0.3) +
    geom_point(aes(y=avgAF, color="blue"), size = 0.2, stroke = 0) +
	labs(title="The average best fitness and average average fitness\nwith 95% confidence intervals, averaged over 50 runs (1a)", x ="Generation", y = "Average fitness") +
	scale_x_continuous(breaks = seq(0, 100, 10)) +
	scale_y_continuous(breaks = seq(0, 200, 5)) +
	scale_colour_manual(values=c("blue", "black"), 
                       name="Fitness Type",
                       labels=c("Best fitness", "Average fitness"))

ggsave("Average fitness 6.png", width = 7, height = 4)



ggplot(x_avg, aes(x=G)) +
	geom_line(aes(y=stdBF, color="black")) + 
    geom_point(aes(y=stdBF, color="black"), size = 0.2, stroke = 0) +
	geom_line(aes(y=stdAF, color="blue")) + 
    geom_point(aes(y=stdAF, color="blue"), size = 0.2, stroke = 0) +
	labs(title="The standard deviation of average best fitness and average average fitness,\naveraged over 50 runs (1a)", x ="Generation", y = "Standard deviation") +
	scale_x_continuous(breaks = seq(0, 100, 10)) +
	scale_y_continuous(breaks = seq(0, 10, 0.5)) +
	scale_colour_manual(values=c("blue", "black"), 
                       name="Fitness Type",
                       labels=c("Standard deviation\nof best fitness\n", "Standard deviation\nof average fitness"))

ggsave("Standard deviation 1a.png", width = 7, height = 4)



# 1. b)

	
x_overall_stats <- x_run_stats %>% 
	summarize(bestFitness = max(bestFit), avgF = mean(bestFit), stdF = sd(bestFit), CI_low = t.test(bestFit)$"conf.int"[1],
		CI_high = t.test(bestFit)$"conf.int"[2])
		
# ----------------------------------------------------------------------------------------------------------------------

# 2.

# 2a pop size = 50, 2b = 200, 2c = 800

x <- read.csv("onemax_stats.csv")

x_gen_stats_2b <- x %>% 
	group_by(R, G) %>% 
	summarize(bestF = max(F), avgF = mean(F), stdF = sd(F), CI = (t.test(F)$"conf.int"[2] - t.test(F)$"conf.int"[1])/2)
	
x_run_stats_2b <- x_gen_stats_2b %>% 
	group_by(R) %>% 
	summarize(bestFit = max(bestF), avgF = mean(bestF), stdF = sd(bestF), CI = (t.test(bestF)$"conf.int"[2] - t.test(bestF)$"conf.int"[1])/2)
	
x_overall_stats_2b <- x_run_stats_2b %>% 
	summarize(bestFitness = max(bestFit), avgF = mean(bestFit), stdF = sd(bestFit), CI = (t.test(bestFit)$"conf.int"[2] - t.test(bestFit)$"conf.int"[1])/2)



x_avg_2a <- x_gen_stats_2a %>% 
	group_by(G) %>% 
	summarize(avgBF = mean(bestF), stdBF = sd(bestF), BCI = (t.test(bestF)$"conf.int"[2] - t.test(bestF)$"conf.int"[1])/2,
				avgAF = mean(avgF), stdAF = sd(avgF), ACI = (t.test(avgF)$"conf.int"[2] - t.test(avgF)$"conf.int"[1])/2)
				
x_avg_2b <- x_gen_stats_2b %>% 
	group_by(G) %>% 
	summarize(avgBF = mean(bestF), stdBF = sd(bestF), BCI = (t.test(bestF)$"conf.int"[2] - t.test(bestF)$"conf.int"[1])/2,
				avgAF = mean(avgF), stdAF = sd(avgF), ACI = (t.test(avgF)$"conf.int"[2] - t.test(avgF)$"conf.int"[1])/2)

x_avg_2c <- x_gen_stats_2c %>% 
	group_by(G) %>% 
	summarize(avgBF = mean(bestF), stdBF = sd(bestF), BCI = (t.test(bestF)$"conf.int"[2] - t.test(bestF)$"conf.int"[1])/2,
				avgAF = mean(avgF), stdAF = sd(avgF), ACI = (t.test(avgF)$"conf.int"[2] - t.test(avgF)$"conf.int"[1])/2)

x_avg_2d <- x_gen_stats_2d %>% 
	group_by(G) %>% 
	summarize(avgBF = mean(bestF), stdBF = sd(bestF), BCI = (t.test(bestF)$"conf.int"[2] - t.test(bestF)$"conf.int"[1])/2,
				avgAF = mean(avgF), stdAF = sd(avgF), ACI = (t.test(avgF)$"conf.int"[2] - t.test(avgF)$"conf.int"[1])/2)


ggplot() +
	geom_line(data = x_avg_2a, aes(x=G, y=avgBF, color="green")) + 
    geom_errorbar(data = x_avg_2a, aes(x=G, ymin=avgBF-BCI, ymax=avgBF+BCI), color="#00db75", size=0.3) +
	geom_line(data = x_avg_2b, aes(x=G, y=avgBF, color="blue")) + 
    geom_errorbar(data = x_avg_2b, aes(x=G, ymin=avgBF-BCI, ymax=avgBF+BCI), color="black", size=0.3) +
	geom_line(data = x_avg_2c, aes(x=G, y=avgBF, color="black")) + 
    geom_errorbar(data = x_avg_2c, aes(x=G, ymin=avgBF-BCI, ymax=avgBF+BCI), color="blue", size=0.3) +
	labs(title="The average best fitness of different crossover rates\nwith 95% confidence intervals, averaged over 50 runs (4)", x ="Generation", y = "Average best fitness") +
	scale_x_continuous(breaks = seq(0, 100, 10)) +
	scale_y_continuous(breaks = seq(0, 200, 5)) +
	scale_colour_manual(values=c("blue", "black", "green"), 
                       name="Crossover Rate",
                       labels=c("0.7", "1", "0.4"))

ggsave("Average fitness 4.png", width = 7, height = 4)





ggplot() +
	geom_line(data = x_avg_2a, aes(x=G, y=avgBF, color="green")) + 
    geom_errorbar(data = x_avg_2a, aes(x=G, ymin=avgBF-BCI, ymax=avgBF+BCI), color="#00db75", size=0.3) +
	geom_line(data = x_avg_2b, aes(x=G, y=avgBF, color="blue")) + 
    geom_errorbar(data = x_avg_2b, aes(x=G, ymin=avgBF-BCI, ymax=avgBF+BCI), color="black", size=0.3) +
	geom_line(data = x_avg_2c, aes(x=G, y=avgBF, color="black")) + 
    geom_errorbar(data = x_avg_2c, aes(x=G, ymin=avgBF-BCI, ymax=avgBF+BCI), color="blue", size=0.3) +
	geom_line(data = x_avg_2d, aes(x=G, y=avgBF, color="red")) + 
    geom_errorbar(data = x_avg_2d, aes(x=G, ymin=avgBF-BCI, ymax=avgBF+BCI), color="red", size=0.3) +
	labs(title="The average best fitness of different selection types\nwith 95% confidence intervals, averaged over 50 runs (5)", x ="Generation", y = "Average best fitness") +
	scale_x_continuous(breaks = seq(0, 100, 10)) +
	scale_y_continuous(breaks = seq(0, 200, 10)) +
	scale_colour_manual(values=c("blue", "black", "green", "red"), 
                       name="Crossover Rate",
                       labels=c("Rank", "Tournament", "Fitness proportional", "Random"))

ggsave("Average fitness 5.png", width = 7, height = 4)



# ----------------------------------------------------------------------------------------------------------------------------

# 6


x <- read.csv("onemax_stats.csv")

x_gen_stats <- x %>% 
	group_by(R, G) %>% 
	summarize(bestF = max(F), avgF = mean(F))
	
x_run_stats <- x_gen_stats %>% 
	group_by(R) %>% 
	summarize(bestFit = max(bestF), avgF = mean(bestF), stdF = sd(bestF))
	
x_overall_stats <- x_run_stats %>% 
	summarize(bestFitness = max(bestFit), avgF = mean(bestFit), stdF = sd(bestFit))
	
x_opt <- x[which(x$F == 200),] %>% 
	group_by(R) %>% 
	summarize(minG = min(G), avgG = mean(G), stdG = sd(G), GCI = (CI(G, ci = 0.95)[1] - CI(G, ci = 0.95)[2]))

x_opt_overall_stats <- x_opt %>% 
	summarize(avgGen = mean(minG), stdF = sd(minG), CI_low = t.test(minG)$"conf.int"[1], CI_high = t.test(minG)$"conf.int"[2])



x_avg <- x_gen_stats %>% 
	group_by(G) %>% 
	summarize(avgBF = mean(bestF), stdBF = sd(bestF), BCI = (CI(bestF, ci = 0.95)[1] - CI(bestF, ci = 0.95)[2]),
				avgAF = mean(avgF), stdAF = sd(avgF), ACI = (CI(avgF, ci = 0.95)[1] - CI(avgF, ci = 0.95)[2]))
				

ggplot(x_avg, aes(x=G)) +
	geom_line(aes(y=avgBF, color="black")) + 
    geom_errorbar(aes(ymin=avgBF-BCI, ymax=avgBF+BCI), color="#ff2e2e", size=0.3) +
    geom_point(aes(y=avgBF, color="black"), size = 0.2, stroke = 0) +
	geom_line(aes(y=avgAF, color="blue")) + 
    geom_errorbar(aes(ymin=avgAF-ACI, ymax=avgAF+ACI), color="#ff2e2e", size=0.3) +
    geom_point(aes(y=avgAF, color="blue"), size = 0.2, stroke = 0) +
	labs(title="The average best fitness and average average fitness\nwith 95% confidence intervals, averaged over 50 runs (6)", x ="Generation", y = "Average fitness") +
	scale_x_continuous(breaks = seq(0, 100, 10)) +
	scale_y_continuous(breaks = seq(0, 210, 10)) +
	scale_colour_manual(values=c("blue", "black"), 
                       name="Fitness Type",
                       labels=c("Best fitness", "Average fitness"))

ggsave("Average fitness 6.png", width = 7, height = 4)



ggplot(x_avg, aes(x=G)) +
	geom_line(aes(y=stdBF, color="black")) + 
    geom_point(aes(y=stdBF, color="black"), size = 0.2, stroke = 0) +
	geom_line(aes(y=stdAF, color="blue")) + 
    geom_point(aes(y=stdAF, color="blue"), size = 0.2, stroke = 0) +
	labs(title="The standard deviation of average best fitness and average average fitness,\naveraged over 50 runs (6)", x ="Generation", y = "Standard deviation") +
	scale_x_continuous(breaks = seq(0, 100, 10)) +
	scale_y_continuous(breaks = seq(0, 10, 0.5)) +
	scale_colour_manual(values=c("blue", "black"), 
                       name="Fitness Type",
                       labels=c("Standard deviation\nof best fitness\n", "Standard deviation\nof average fitness"))

ggsave("Standard deviation 6.png", width = 7, height = 4)











