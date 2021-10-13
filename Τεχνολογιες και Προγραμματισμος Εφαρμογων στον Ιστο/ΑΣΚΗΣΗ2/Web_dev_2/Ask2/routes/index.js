const express = require('express')
const router = express.Router()
router.use(express.urlencoded({ extended: true }))
router.use(express.json())
const fetch = require('node-fetch')
book = ""

router.get('/', (_req, res) => {
    res.render("main.hbs")
})

router.get('/FindBook', (_req, res) => {
    res.render("FindBook.hbs")
})

router.get('/MyBooks', (_req, res) => {
    res.render("MyBooks.hbs")
})

router.get('/ModifyBook', (_req, res) => {
    res.render("ModifyBook.hbs")
})

router.post('/FindBook', (_req,res) => {
    //console.log(_req.body)

    const book = _req.body
    res.json({
        author:book.author,
        title:book.title,
        bookid:book.bookid
    })
}) 




module.exports = router

