package pt.thehentai

import eu.kanade.tachiyomi.source.model.SManga
import eu.kanade.tachiyomi.source.model.SChapter
import eu.kanade.tachiyomi.source.model.Page
import eu.kanade.tachiyomi.source.online.HttpSource
import okhttp3.Request
import org.jsoup.nodes.Document

class TheHentai : HttpSource() {

    override val name = "TheHentai"
    override val baseUrl = "https://thehentai.net"
    override val lang = "pt-BR"
    override val supportsLatest = true

    override fun popularMangaRequest(page: Int): Request {
        return GET("$baseUrl/manga/?page=$page")
    }

    override fun popularMangaSelector() = "div.manga-item"
    override fun popularMangaFromElement(element: org.jsoup.nodes.Element): SManga {
        val manga = SManga.create()
        manga.title = element.select("h3.title").text()
        manga.thumbnail_url = element.select("img").attr("abs:src")
        manga.url = element.select("a").attr("href")
        return manga
    }

    override fun popularMangaNextPageSelector() = "a.next"

    override fun latestUpdatesRequest(page: Int) = popularMangaRequest(page)
    override fun latestUpdatesSelector() = popularMangaSelector()
    override fun latestUpdatesFromElement(element: org.jsoup.nodes.Element) = popularMangaFromElement(element)
    override fun latestUpdatesNextPageSelector() = popularMangaNextPageSelector()

    override fun searchMangaRequest(page: Int, query: String) = GET("$baseUrl/search?query=$query&page=$page")
    override fun searchMangaSelector() = popularMangaSelector()
    override fun searchMangaFromElement(element: org.jsoup.nodes.Element) = popularMangaFromElement(element)
    override fun searchMangaNextPageSelector() = popularMangaNextPageSelector()

    override fun mangaDetailsRequest(manga: SManga) = GET(baseUrl + manga.url)
    override fun mangaDetailsParse(document: Document): SManga {
        val manga = SManga.create()
        manga.title = document.selectFirst("h1.title")?.text() ?: ""
        manga.author = document.selectFirst("span.author")?.text()
        manga.artist = document.selectFirst("span.artist")?.text()
        manga.genre = document.select("span.genres a").joinToString { it.text() }
        manga.description = document.selectFirst("div.description")?.text()
        manga.status = SManga.UNKNOWN
        return manga
    }

    override fun chapterListRequest(manga: SManga) = GET(baseUrl + manga.url)
    override fun chapterListSelector() = "ul.chapters li"
    override fun chapterFromElement(element: org.jsoup.nodes.Element): SChapter {
        val chapter = SChapter.create()
        chapter.name = element.select("a").text()
        chapter.url = element.select("a").attr("href")
        return chapter
    }

    override fun pageListRequest(chapter: SChapter) = GET(baseUrl + chapter.url)
    override fun pageListParse(document: Document): List<Page> {
        return document.select("div.page img").mapIndexed { i, element ->
            Page(i, "", element.attr("abs:src"))
        }
    }

    override fun imageUrlRequest(page: Page) = throw UnsupportedOperationException()
    override fun imageUrlParse(response: okhttp3.Response) = throw UnsupportedOperationException()
}