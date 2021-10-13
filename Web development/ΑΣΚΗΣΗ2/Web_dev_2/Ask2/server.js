
const express = require('express')
const app = express()

const exphbs = require('express-handlebars')

const indexRouter = require('./routes/index')



//handlebars
app.set('view.egine', 'hbs')
app.set('views',__dirname + '/views')
app.set('layout', '/layouts/layout')

app.use(express.static('public'))
app.use(express.urlencoded({ extended: true }))
app.use(express.json())
app.use('/', indexRouter)

app.engine('hbs', exphbs({
    extname:'hbs',
    defaultLayout: 'index',
    layoutsDir: __dirname + '/views/layouts',
    partialsDir: __dirname + '/views/partials'

}))

const port = 3000
app.listen(port)
console.log("Listening to server : http://localhost:3000${port}")


