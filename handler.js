'use strict';

const axios = require('axios').default;
const xml2js = require('xml2js');
const { TwitterApi } = require('twitter-api-v2');
const http = require('http');

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

  const today = new Date().toISOString().split("T")[0];

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

    // Get our desired newspapers by name and date
    editions = editions.filter(x => Object.keys(newspapers).indexOf(x["name"]) > -1 && x["publish_date"] == today);
    
    // And save them elsewhere
    covers = covers.concat(editions);
  }

  this.generateTweets(covers);

  callback(null, JSON.stringify(covers));
};

module.exports.generateTweets = async (covers) => {
  let twitterClient = new TwitterApi({
    appKey: process.env.TWITTER_API_KEY,
    appSecret: process.env.TWITTER_API_SECRET_KEY,
    accessToken: process.env.TWITTER_ACCESS_TOKEN,
    accessSecret: process.env.TWITTER_ACCESS_TOKEN_SECRET
  });

  for (const cover of covers) {
    const category = categories[cover["name"]];
    const handle = newspapers[cover["name"]];

    const status = `#${category} ${cover["name"]} - ${cover["publish_date"]} ${handle}`;

    const sendTweet = async (err, imageBuffer) => {
      if (err) {
        throw err;
      }

      try {
        const mediaIds = await twitterClient.v1.uploadMedia(imageBuffer, { type: 'png' })
      
        await twitterClient.v2.tweet({
          text: status,
          media: { media_ids: [mediaIds] }
        })
      }
      catch (e) {
        console.error(e)
      }
    }

    getImage(cover["image_url"], sendTweet);
  } 
}

function getImage(url, callback) {
  http.get(url, res => {
      const bufs = [];
      res.on('data', (chunk) => {
          bufs.push(chunk);
      });
      res.on('end', () => {
          const data = Buffer.concat(bufs);
          callback(null, data);
      });
  }).on('error', callback);
}
