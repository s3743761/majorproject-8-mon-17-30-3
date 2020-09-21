import React, { Component } from 'react'
import './CSS/AdminHome.css'
import logo from './CSS/logo.png'

class AdminHome extends Component {

        render() {
        return (
            <div className="Home">
             <head>  
              
                   <title>View Bookings</title>
              
                   </head> 
              <body>
                  <header>
                      <div className="wrapper">
                          <div className="logo">
                          <img src={logo} alt="">
                          </img>
                              </div>
                             
                          </div>
                          <ul className="nav-area">
                              <li><a href="/Login">Logout</a></li>
                              </ul>
                              <div className="welcome-text">
                                  <h1> 
                                  <a href="#">Assign Tasks</a><br></br>
                                  <a href="/AllBookings">View Bookings</a><br></br>
                                  <a href="#">Calendar</a><br></br>
                                  <a href="/Profile">View Details</a><br></br>
                                      </h1>
                                  </div>
                      </header>

              </body>
            </div>

        )
    }
}
export default AdminHome
