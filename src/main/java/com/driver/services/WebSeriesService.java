package com.driver.services;

import com.driver.EntryDto.WebSeriesEntryDto;
import com.driver.model.ProductionHouse;
import com.driver.model.WebSeries;
import com.driver.repository.ProductionHouseRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WebSeriesService {

    @Autowired
    WebSeriesRepository webSeriesRepository;

    @Autowired
    ProductionHouseRepository productionHouseRepository;

    public Integer addWebSeries(WebSeriesEntryDto webSeriesEntryDto)throws  Exception{

        //Add a webSeries to the database and update the ratings of the productionHouse
        //Incase the seriesName is already present in the Db throw Exception("Series is already present")
        //use function written in Repository Layer for the same
        //Dont forget to save the production and webseries Repo

        ProductionHouse productionHouse = productionHouseRepository.findById(webSeriesEntryDto.getProductionHouseId()).get();

        for(WebSeries currwebSeries: productionHouse.getWebSeriesList()){
            if(currwebSeries.getSeriesName() == webSeriesEntryDto.getSeriesName()){
                throw new Exception("Series is already present");
            }
        }

        WebSeries webSeries = new WebSeries();

        webSeries.setSeriesName(webSeriesEntryDto.getSeriesName());
        webSeries.setAgeLimit(webSeriesEntryDto.getAgeLimit());
        webSeries.setRating(webSeriesEntryDto.getRating());
        webSeries.setSubscriptionType(webSeriesEntryDto.getSubscriptionType());
        webSeries.setProductionHouse(productionHouse);


        double newRating = (webSeries.getRating()+ productionHouse.getRatings())/productionHouse.getWebSeriesList().size();
        productionHouse.setRatings(newRating);
        productionHouse.getWebSeriesList().add(webSeries);
        WebSeries savedWebSeries = webSeriesRepository.save(webSeries);

        productionHouseRepository.save(productionHouse);

        return savedWebSeries.getId();
    }

}
