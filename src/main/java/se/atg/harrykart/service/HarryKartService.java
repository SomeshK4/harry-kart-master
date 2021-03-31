package se.atg.harrykart.service;


import se.atg.harrykart.dto.Response;

public interface HarryKartService {

    Response calculateRanking(String request) throws Exception;

}
