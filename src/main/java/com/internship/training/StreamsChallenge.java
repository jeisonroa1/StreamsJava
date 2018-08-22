package com.internship.training;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.DoubleToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.IntStream;
import java.util.Comparator;

public class StreamsChallenge {
    
    private List<Series> listaSeries;

    public void executeChallenge() {
        getEpisodesTotalCount();
        getAverageEpisodesCount();
        getMaxEpisodeCount();
        getBest10SeriesByRating();
        getAllGenres();
        getSeriesByStudioShaft();
        getMostEpisodesSeries();
        getBestStudio();
        getBestGenre();
        getWorstGenre();
        getTop5MostCommmonEpisodeCount();
        getAverageRatingOfCommedySeries();
        getMostCommonGenreWhereSugitaTomokazuActs();
        getBestActor();
        getBestShounenStudio();
    }

    public StreamsChallenge(){
        init();
    }

    private void init(){
        try {
            ObjectMapper mapper = new ObjectMapper();
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource("series.json").getFile());
            List<Series> importedSeries = mapper.readValue(file,new TypeReference<List<Series>>(){});
            this.listaSeries = importedSeries.stream().filter(x->x.getRating()>0).collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getEpisodesTotalCount(){
        System.out.println("------------------------------------------------------------");

        int count = 0;
        count=listaSeries.stream()
                        .mapToInt(i -> i.getEpisodes())
                        .sum();

        System.out.println(String.format("Total episodes: %d",count));
        System.out.println("------------------------------------------------------------");
    }

    private void getAverageEpisodesCount(){
        System.out.println("------------------------------------------------------------");
        double average = 0;
        average=listaSeries.stream()
                            .mapToInt(i -> i.getEpisodes())
                            .average()
                            .getAsDouble();

        System.out.println(String.format("Average number of episodes: %f",average));
        System.out.println("------------------------------------------------------------");
    }

    private void getMaxEpisodeCount(){
        System.out.println("------------------------------------------------------------");
        int count = 0;
        count = listaSeries.stream()
                            .mapToInt( i -> i.getEpisodes())
                            .max()
                            .getAsInt();

        System.out.println(String.format("Max number of episodes: %d",count));
        System.out.println("------------------------------------------------------------");

    }

    private void getBest10SeriesByRating(){
        System.out.println("------------------------------------------------------------");
        System.out.println("These are the top 10 series:");
        //TODO print the name of the top 10 series - one by line
        List <String> top;

        listaSeries.stream()
                        .sorted(    (s1, s2) -> Double.valueOf(s2.getRating())
                            .compareTo(Double.valueOf(s1.getRating())))
                        .limit(10)
                        .forEach(j -> System.out.println(j.getName()));
        System.out.println("------------------------------------------------------------");
    }

    private void getAllGenres(){
        System.out.println("------------------------------------------------------------");
        System.out.println("These are all the genres found:");
        //TODO print all the genres of the series sorted alphabetically, they can not be repeated - one by line
        listaSeries.stream()
                .map(i -> i.getGenres())
                    .flatMap(Collection::stream)
                    .distinct()
                    .sorted()
                    .forEach(j -> System.out.println(j));
        //System.out.println(genres);
        System.out.println("------------------------------------------------------------");
    }

    private void getSeriesByStudioShaft(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Series by Studio Shaft:");
        //TODO print the name of all Studio 'Shaft' series - one by line
        listaSeries.stream()
                    .filter( i ->  i.getStudios().contains("Shaft")== true)
                    .forEach(j -> System.out.println(j.getName()));


        System.out.println("------------------------------------------------------------");
    }

    private void getMostEpisodesSeries(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Show with most episodes:");
        //TODO print the name and the episode count of the show with the most episodes
        listaSeries.stream()
                    .sorted(Comparator.comparing(Series::getEpisodes).reversed())
                    .limit(1)
                    .forEach(j -> System.out.println(j.getName() + " Episodes: " + j.getEpisodes()));

        System.out.println("------------------------------------------------------------");
    }

    private void getBestStudio(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Best Studio:");
        //TODO print the name and the average rating of the best Studio
        listaSeries.stream().flatMap(i -> i.getStudios().stream()
                                                        .map(j -> new Pair<>(i.getRating(),j)))
                            .collect(Collectors.groupingBy(Pair::getValue, Collectors.averagingDouble(Pair::getKey)))
                            .entrySet()
                            .stream()
                            .sorted( Map.Entry.<String,Double> comparingByValue().reversed())
                            .limit(1)
                            .forEach(j -> System.out.println("Studio: "+ j.getKey() + " Average Rating: "+ j.getValue()));

        System.out.println("------------------------------------------------------------");
    }

    private void getBestGenre(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Best genre:");
        //TODO print the name and the average rating of the best Genre
        listaSeries.stream().flatMap(i -> i.getGenres().stream()
                                                        .map(j -> new Pair<>(i.getRating(),j)))
                .collect(Collectors.groupingBy(Pair::getValue, Collectors.averagingDouble(Pair::getKey)))
                .entrySet()
                .stream()
                .sorted( Map.Entry.<String,Double> comparingByValue().reversed())
                .limit(1)
                .forEach(j -> System.out.println("Best Genre: "+ j.getKey() + " Average Rating: "+ j.getValue()));

        System.out.println("------------------------------------------------------------");
    }

    private void getWorstGenre(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Worst genre:");
        //TODO print the name and the average rating of the worst Genre
        listaSeries.stream().flatMap(i -> i.getGenres().stream()
                                                        .map(j -> new Pair<>(i.getRating(),j)))
                .collect(Collectors.groupingBy(Pair::getValue, Collectors.averagingDouble(Pair::getKey)))
                .entrySet()
                .stream()
                .sorted( Map.Entry.<String,Double> comparingByValue())
                .limit(1)
                .forEach(j -> System.out.println("Worst Genre: "+ j.getKey() + " Average Rating: "+ j.getValue()));
        System.out.println("------------------------------------------------------------");
    }

    private void getTop5MostCommmonEpisodeCount(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Top 5 episode count:");
        //TODO print the count and value of the of the top 5 most common episode count  - example:  100 shows have 25 episodes
        listaSeries.stream()
                .collect(Collectors.groupingBy(Series::getEpisodes, Collectors.counting()))
                .entrySet()
                .stream()
                .sorted( Map.Entry.<Integer,Long> comparingByValue().reversed())
                .limit(5)
                .forEach(j -> System.out.println(j.getValue() + " Shows have " + j.getKey() + " Episodes "));
        System.out.println("------------------------------------------------------------");
    }

    private void getAverageRatingOfCommedySeries(){
        System.out.println("------------------------------------------------------------");
        double average = 0;
        //TODO get the average rating of the 'Comedy' shows, put the result in average
        average = listaSeries.stream()
                             .filter(k -> k.getGenres().contains("Comedy"))
                             .mapToDouble(i -> i.getRating())
                             .average()
                             .getAsDouble();
        System.out.println(String.format("Average rating of comedy series: %f",average));
        System.out.println("------------------------------------------------------------");
    }

    private void getMostCommonGenreWhereSugitaTomokazuActs(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Sugita Tomokazu most common genre:");
        //TODO print the most common genre where 'Sugita,Tomokazu' acts - the most common genre of the shows where 'Sugita,Tomokazu' is in mainCast
        String h = listaSeries.stream()
                    .filter( k -> k.getMainCast().contains("Sugita,Tomokazu")== true )
                    .flatMap(i -> i.getGenres().stream())
                    .collect(Collectors.groupingBy(String::valueOf, Collectors.counting()))
                    .entrySet()
                    .stream()
                    //.forEach(f -> System.out.println(f.getKey() + f.getValue()))
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .toString();
                    System.out.println(h);
        System.out.println("------------------------------------------------------------");
    }

    private void getBestActor(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Best Actor:");
        //TODO print the name and the average rating of the best Actor
        listaSeries.stream().flatMap(i -> i.getMainCast().stream()
                .map(j -> new Pair<>(j,i.getRating())))

                .collect(Collectors.groupingBy(Pair::getKey, Collectors.averagingDouble(Pair::getValue)))
                .entrySet()
                .stream()
                .sorted( Map.Entry.<String,Double> comparingByValue().reversed())
                .limit(1)
                .forEach(j -> System.out.println("Best Actor: "+ j.getKey() + " Average Rating: "+ j.getValue()));
        System.out.println("------------------------------------------------------------");

    }

    private void getBestShounenStudio(){
        System.out.println("------------------------------------------------------------");
        System.out.println("Best Shounen Studio:");
        //TODO print the name and the average rating (of the shounen series) of the best Shounen Studio - the studio with the best 'Shounen' (genre) series
        listaSeries.stream()
                .filter(s->s.getGenres().contains("Shounen"))
                .flatMap(x-> x.getStudios().stream()
                                        .map(y ->new Pair<>(y,x.getRating())))
                .collect(Collectors.groupingBy(Pair::getKey,Collectors.averagingDouble(Pair::getValue)))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(1)
                .forEach(System.out::println);

        System.out.println("------------------------------------------------------------");

    }




}
