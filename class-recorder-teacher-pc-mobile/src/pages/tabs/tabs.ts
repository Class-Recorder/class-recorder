import { Component } from '@angular/core';

import { ServerIpPage } from '../server-ip/server-ip';
import { HomePage } from '../home/home';

@Component({
  templateUrl: 'tabs.html'
})
export class TabsPage {

  tab1Root = ServerIpPage;
  tab2Root = HomePage;

  constructor() {

  }
}
