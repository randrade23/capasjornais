#!/usr/bin/python3

from xml.dom.minidom import parse
import xml.dom.minidom

import glob, os, sys, datetime

try:
    from urllib.request import urlopen
except ImportError:
    from urllib2 import urlopen

#create twitter oauth
from twython import Twython
twitter = Twython('xx','xx','xx', 'xx')

#magic function
def tweetJornais(f):
	DOMTree = xml.dom.minidom.parse(f)
	collection = DOMTree.documentElement
	covers = collection.getElementsByTagName("bj_related_image")
	for cover in covers:
		nameNode = cover.getElementsByTagName("name")[0]
		name = nameNode.childNodes[0].data
		idNode = cover.getElementsByTagName("id")[0]
		idnum = idNode.childNodes[0].data
		if name in jornais and idnum != '10017':
			dataNode = cover.getElementsByTagName("publish_date")[0]
			data = dataNode.childNodes[0].data
			if data == str(datetime.date.today()):
				#download cover
				urlNode = cover.getElementsByTagName("image_url")[0]
				url = urlNode.childNodes[0].data
				coverfile = urlopen(url)
				output = open(name + ".png", "wb")
				output.write(coverfile.read())
				output.close()
				#cover downloaded
				photo = open(name + ".png", "rb")
				twitter.update_status_with_media(status='#' + categorias[name] + ' ' + name + " - " + data + " " + jornais[name], media=photo)

#newspaper list
jornais = {"Expresso":"@expresso", "SOL":"@solonline", "Correio da Manhã":"@cmjornal", "Jornal de Notícias":"@jornalnoticias", "Público":"@publico", "Diário de Notícias":"@dntwit", "i":"@itwitting", "Diário Económico":"@diarioeconomico", "Jornal de Negócios":"@JNegocios", "O Jogo":"@ojogo", "A Bola":"@abolapt", "Record":"@Record_Portugal"}
categorias = {"Expresso":"Notícias", "SOL":"Notícias", "Correio da Manhã":"Notícias", "Jornal de Notícias":"Notícias", "Público":"Notícias", "Diário de Notícias":"Notícias", "i":"Notícias", "Diário Económico":"Economia", "Jornal de Negócios":"Economia", "O Jogo":"Desporto", "A Bola":"Desporto", "Record":"Desporto"}
files = {"National":"generalistas.xml", "Sport":"desporto.xml", "Economy":"economia.xml"}

#cleanup png and xml
filelist = glob.glob("*.png")
for f in filelist:
    os.remove(f)
filelist = glob.glob("*.xml")
for f in filelist:
    os.remove(f)

#work your magic
for cat in files:
	xmlfile = urlopen("http://services.sapo.pt/News/NewsStand/" + cat)
	output = open(files[cat],'wb')
	output.write(xmlfile.read())
	output.close()
	tweetJornais(files[cat])
