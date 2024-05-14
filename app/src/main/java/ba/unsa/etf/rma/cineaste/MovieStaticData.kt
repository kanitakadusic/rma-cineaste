package ba.unsa.etf.rma.cineaste

fun getFavoriteMovies(): List<Movie> {
    return listOf(
        Movie(
            1001,
            "Pride and prejudice",
            "Sparks fly when spirited Elizabeth Bennet meets single, rich, and proud Mr. Darcy. But Mr. Darcy reluctantly finds himself falling in love with a woman beneath his class. Can each overcome their own pride and prejudice?",
            "16.02.2005.",
            "https://www.imdb.com/title/tt0414387/",
            "https://www.imdb.com/title/tt0414387/",
            "https://www.imdb.com/title/tt0414387/"
        ),
        Movie(
            1002,
            "La La Land",
            "While navigating their careers in Los Angeles, a pianist and an actress fall in love while attempting to reconcile their aspirations for the future.",
            "09.12.2016.",
            "https://www.imdb.com/title/tt3783958/",
            "https://www.imdb.com/title/tt3783958/",
            "https://www.imdb.com/title/tt3783958/"
        ),
        Movie(
            1003,
            "Good Will Hunting",
            "Will Hunting, a janitor at M.I.T., has a gift for mathematics, but needs help from a psychologist to find direction in his life.",
            "05.12.1997.",
            "https://www.imdb.com/title/tt0119217/",
            "https://www.imdb.com/title/tt0119217/",
            "https://www.imdb.com/title/tt0119217/"
            )
    )
}

fun getRecentMovies(): List<Movie> {
    return listOf(
        Movie(
            2001,
            "Dune: Part Two",
            "Paul Atreides unites with Chani and the Fremen while seeking revenge against the conspirators who destroyed his family.",
            "01.03.2024.",
            "https://www.imdb.com/title/tt15239678/",
            "https://www.imdb.com/title/tt15239678/",
            "https://www.imdb.com/title/tt15239678/"
            ),
        Movie(
            2002,
            "Oppenheimer",
            "The story of American scientist J. Robert Oppenheimer and his role in the development of the atomic bomb.",
            "21.07.2023.",
            "https://www.imdb.com/title/tt15398776/",
            "https://www.imdb.com/title/tt15398776/",
            "https://www.imdb.com/title/tt15398776/"
            ),
        Movie(
            2003,
            "Doctor Strange in the Multiverse of Madness",
            "Doctor Strange teams up with a mysterious teenage girl from his dreams who can travel across multiverses, to battle multiple threats, including other-universe versions of himself, which threaten to wipe out millions across the multiverse. They seek help from Wanda the Scarlet Witch, Wong and others.",
            "06.05.2022.",
            "https://www.imdb.com/title/tt9419884/",
            "https://www.imdb.com/title/tt9419884/",
            "https://www.imdb.com/title/tt9419884/"
            )
    )
}

fun getMovieActors(): Map<String,List<String>> {
    return mapOf("Pride and prejudice" to listOf("Keira Knightley", "Talulah Riley", "Rosamund Pike"))
}

fun getSimilarMovies(): Map<String,List<String>> {
    return mapOf("Pride and prejudice" to listOf("Jane Eyre", "The Notebook", "Atonement"))
}