import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import {ChatbotService} from "../services/chatbot.service";

@Component({
  selector: 'app-chatbot',
  templateUrl: './chatbot.component.html',
  styleUrls: ['./chatbot.component.css']
})
export class ChatbotComponent implements OnInit {
  query: string = "";
  messages: Array<{type: string, content: string}> = [];
  loading: boolean = false;

  @ViewChild('chatContainer') chatContainer!: ElementRef;

  constructor(private chatbotService: ChatbotService) { }

  ngOnInit(): void {
    this.messages.push({type: 'bot', content: 'Hello! I am your banking assistant. How can I help you today?'});
  }

  sendMessage() {
    if (this.query.trim() === "") return;
    
    let userMsg = this.query;
    this.messages.push({type: 'user', content: userMsg});
    this.query = "";
    this.loading = true;
    this.scrollToBottom();

    this.chatbotService.ask(userMsg).subscribe({
      next: (resp) => {
        this.messages.push({type: 'bot', content: resp});
        this.loading = false;
        this.scrollToBottom();
      },
      error: (err) => {
        this.messages.push({type: 'bot', content: "Sorry, I am having trouble connecting to the brain."});
        this.loading = false;
        this.scrollToBottom();
      }
    });
  }

  private scrollToBottom() {
    setTimeout(() => {
      this.chatContainer.nativeElement.scrollTop = this.chatContainer.nativeElement.scrollHeight;
    }, 100);
  }
}
