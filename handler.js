'use strict';

const axios = require('axios').default;
const xml2js = require('xml2js');
const twitter = require('twitter');

const baseUrl = "http://services.sapo.pt/News/NewsStand/";

const newspapers = 
  {
    "Expresso": "@expresso", 
    "SOL": "@solonline", 
    "Correio da Manhã": "@cmjornal", 
    "Jornal de Notícias": "@jornalnoticias", 
    "Público": "@publico", 
    "Diário de Notícias": "@dntwit", 
    "i": "@itwitting", 
    "Diário Económico": "@diarioeconomico", 
    "Jornal de Negócios": "@JNegocios", 
    "O Jogo": "@ojogo", 
    "A Bola": "@abolapt", 
    "Record": "@Record_Portugal"
  }

const categories = 
  {
    "Expresso": "Notícias", 
    "SOL": "Notícias", 
    "Correio da Manhã": "Notícias", 
    "Jornal de Notícias": "Notícias", 
    "Público": "Notícias", 
    "Diário de Notícias": "Notícias", 
    "i": "Notícias", 
    "Diário Económico": "Economia", 
    "Jornal de Negócios": "Economia", 
    "O Jogo": "Desporto", 
    "A Bola": "Desporto", 
    "Record": "Desporto"
  }

const endpoints = ["National", "Sport", "Economy"]

module.exports.getNewspapers = async (event, context, callback) => {
  let responses = [];
  let parsedResponses = [];
  let covers = [];

  // Get all newsstands
  for (const endpoint of endpoints) {
    const apiUrl = `${baseUrl}${endpoint}`;
    const response = await axios.get(apiUrl);
    responses.push(response.data);
  }

  // ...and parse them
  for (const response of responses) {
    const parsed = await xml2js.parseStringPromise(response, {explicitArray : false});
    parsedResponses.push(parsed);
  }

  for (const response of parsedResponses) {
    // Digging through ugly XML
    let editions = response["newsstand"]["bj_editionsgroup"]["bj_related_image"];

    // Get our desired newspapers
    editions = editions.filter(x => Object.keys(newspapers).indexOf(x["name"]) > -1);
    
    // And save them elsewhere
    covers = covers.concat(editions);
  }

  callback(null, JSON.stringify(covers));
};
