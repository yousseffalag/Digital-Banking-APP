import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ChatbotService {
  private host: string = "http://localhost:8087"; // Corrected port for Chat-bot app

  constructor(private http: HttpClient) { }

  public ask(query: string): Observable<string> {
    return this.http.get(this.host + "/chat", {
      params: { query: query },
      responseType: 'text'
    });
  }
}
